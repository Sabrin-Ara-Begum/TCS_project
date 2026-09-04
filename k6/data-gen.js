import http from 'k6/http';
import { check } from 'k6';

export const options = {
  vus: 1,
  iterations: 1,
};

export default function () {
  const url = 'http://localhost:8080/users';
  const payload = JSON.stringify({
    email: `test${__ITER}@test.com`,
    password: 'password123',
    firstName: 'Test',
    lastName: 'User'
  });
  const params = { headers: { 'Content-Type': 'application/json' } };
  
  let res = http.post(url, payload, params);
  check(res, { 'status is 201': (r) => r.status === 201 });
}