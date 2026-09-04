import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '5s', target: 5 }, // ramp up to 5 VUs
    { duration: '10s', target: 5 }, // stay at 5
    { duration: '5s', target: 0 }, // ramp down
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'],
  },
};

export default function () {
  let res = http.get('http://localhost:8080/restaurants');
  check(res, { 'status was 200': (r) => r.status == 200 });
  sleep(1);
}