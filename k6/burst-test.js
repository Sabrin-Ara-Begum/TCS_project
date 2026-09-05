import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

// Burst Scenario: Sudden traffic spike to 100 concurrent Virtual Users
export const options = {
  scenarios: {
    burst_spike: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '5s', target: 100 },  // Quick spike to 100 VUs in 5s
        { duration: '20s', target: 100 }, // Hold peak burst at 100 VUs for 20s
        { duration: '5s', target: 0 },    // Ramp down to 0 VUs
      ],
      gracefulRampDown: '5s',
    },
  },
  thresholds: {
    // 95% of requests must complete under 1 second (1000ms)
    http_req_duration: ['p(95)<1000'],
    // Failed requests must be strictly less than 5%
    http_req_failed: ['rate<0.05'],
  },
};

export default function () {
  // Realistic workload mix:
  // 60% browse active restaurants
  // 40% inspect individual restaurant details
  let rand = Math.random();

  if (rand < 0.6) {
    let res = http.get(`${BASE_URL}/restaurants`);
    check(res, {
      'get restaurants status 200': (r) => r.status === 200,
    });
  } else {
    // Read cached or first available restaurant
    let listRes = http.get(`${BASE_URL}/restaurants`);
    if (listRes.status === 200) {
      let list = JSON.parse(listRes.body);
      if (list && list.length > 0) {
        let firstId = list[0].id;
        let singleRes = http.get(`${BASE_URL}/restaurants/${firstId}`);
        check(singleRes, {
          'get single restaurant status 200': (r) => r.status === 200,
        });
      }
    }
  }

  sleep(0.2); // Think time between requests
}