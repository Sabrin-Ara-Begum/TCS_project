import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

// Stress Scenario: Escalating load across 5 progressive stages up to 300 VUs
export const options = {
  stages: [
    { duration: '15s', target: 50 },  // Stage 1: Warm up to 50 users
    { duration: '20s', target: 100 }, // Stage 2: Climb to 100 users
    { duration: '25s', target: 200 }, // Stage 3: Climb to 200 users
    { duration: '20s', target: 300 }, // Stage 4: Heavy stress peak at 300 users
    { duration: '15s', target: 0 },   // Stage 5: Recovery and ramp down
  ],
  thresholds: {
    // 95% of requests should respond in under 2 seconds during peak load
    http_req_duration: ['p(95)<2000'],
    // Allow error rate to stay below 10% under extreme 300 VU stress
    http_req_failed: ['rate<0.10'],
  },
};

export default function () {
  let rand = Math.random();

  if (rand < 0.5) {
    // 50% list restaurants
    let res = http.get(`${BASE_URL}/restaurants`);
    check(res, {
      'status is 200': (r) => r.status === 200,
    });
  } else if (rand < 0.8) {
    // 30% fetch menu items
    let listRes = http.get(`${BASE_URL}/restaurants`);
    if (listRes.status === 200) {
      let list = JSON.parse(listRes.body);
      if (list && list.length > 0) {
        let rId = list[Math.floor(Math.random() * list.length)].id;
        let menuRes = http.get(`${BASE_URL}/menu?restaurantId=${rId}`);
        check(menuRes, {
          'menu status is 200': (r) => r.status === 200,
        });
      }
    }
  } else {
    // 20% place an order
    // Simulating order payload
    let listRes = http.get(`${BASE_URL}/restaurants`);
    if (listRes.status === 200) {
      let list = JSON.parse(listRes.body);
      if (list && list.length > 0) {
        let rId = list[0].id;
        let menuRes = http.get(`${BASE_URL}/menu?restaurantId=${rId}`);
        if (menuRes.status === 200) {
          let items = JSON.parse(menuRes.body);
          if (items && items.length > 0) {
            let orderPayload = JSON.stringify({
              userId: "00000000-0000-0000-0000-000000000001", // test dummy ID or captured
              restaurantId: rId,
              deliveryAddress: "K6 Stress Test Address",
              items: [{ menuItemId: items[0].id, quantity: 2 }]
            });
            // Send request (testing server responsiveness)
            let oRes = http.post(`${BASE_URL}/order`, orderPayload, {
              headers: { 'Content-Type': 'application/json' }
            });
            check(oRes, {
              'order request processed': (r) => r.status === 201 || r.status === 400 || r.status === 404,
            });
          }
        }
      }
    }
  }

  sleep(0.3);
}