import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  scenarios: {
    burst: {
      executor: 'per-vu-iterations',
      vus: 20,
      iterations: 5,
      maxDuration: '10s',
    },
  },
};

export default function () {
  let res = http.get('http://localhost:8080/restaurants');
  check(res, { 'status was 200': (r) => r.status == 200 });
  sleep(0.5);
}