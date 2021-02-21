import axios from 'axios';
import { API_BASE_URL } from '../config';

function createInstance() {
  const instance = axios.create({
    baseURL: API_BASE_URL,
    headers: {
      'Content-Type': 'application/json; charset = utf-8',
    },
  });
  return instance;
}

function createFileInstance() {
  const instance = axios.create({
    baseURL: API_BASE_URL,
  });
  return instance;
}

export { createInstance, createFileInstance };
