import http from 'k6/http';
import { check } from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export const options = {
  vus: 1,
  iterations: 1,
};

export default function () {
  const params = { headers: { 'Content-Type': 'application/json' } };

  console.log(`Starting Data Seeding on ${BASE_URL}...`);

  // 1. Create 20 Restaurants
  let restaurantIds = [];
  for (let i = 1; i <= 20; i++) {
    let resPayload = JSON.stringify({
      name: `Restaurant ${i} - Kitchen & Grill`,
      description: `Authentic multi-cuisine dining #${i}`,
      cuisine: i % 2 === 0 ? 'Italian' : 'Continental',
      deliveryFee: 2.50 + (i * 0.25),
      minOrderAmount: 15.00
    });
    let res = http.post(`${BASE_URL}/restaurants`, resPayload, params);
    check(res, { 'restaurant created (201)': (r) => r.status === 201 });
    if (res.status === 201) {
      let rBody = JSON.parse(res.body);
      restaurantIds.push(rBody.id);

      // Add 2 to 3 menu items to this restaurant
      for (let m = 1; m <= 3; m++) {
        let menuPayload = JSON.stringify({
          name: `Special Dish #${m}`,
          description: `Chef special dish ${m} prepared fresh`,
          price: 8.50 + (m * 2.0),
          calories: 400 + (m * 50)
        });
        let mRes = http.post(`${BASE_URL}/restaurants/${rBody.id}/menu-items`, menuPayload, params);
        check(mRes, { 'menu item created (201)': (r) => r.status === 201 });
      }
    }
  }

  // 2. Create 100 Users
  let userIds = [];
  for (let u = 1; u <= 100; u++) {
    let userPayload = JSON.stringify({
      email: `perfuser_${Date.now()}_${u}@impactis.com`,
      password: 'StrongPassword123!',
      firstName: `UserFirstName${u}`,
      lastName: `UserLastName${u}`,
      phone: `+1555000${(1000 + u).toString()}`
    });
    let uRes = http.post(`${BASE_URL}/users`, userPayload, params);
    check(uRes, { 'user created (201)': (r) => r.status === 201 });
    if (uRes.status === 201) {
      userIds.push(JSON.parse(uRes.body).id);
    }
  }

  console.log(`Seeding complete: Created ${restaurantIds.length} restaurants and ${userIds.length} users.`);
}