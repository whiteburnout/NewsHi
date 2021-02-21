import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import vuetify from './plugins/vuetify';
import { ValidationProvider, extend, localize } from 'vee-validate';
import { required, email } from 'vee-validate/dist/rules';
import firebase from 'firebase';
import './styles/tiptap/main.scss';
import VueClipboard from 'vue-clipboard2';
import ko from 'vee-validate/dist/locale/ko.json';
const moment = require('moment');
require('moment/locale/ko');

localize('ko', ko);
extend('required', required);
extend('email', email);
Vue.use(require('vue-moment'), {
  moment,
});

VueClipboard.config.autoSetContainer = true; // add this line
Vue.use(VueClipboard);

// Initialize Firebase
firebase.initializeApp({
  apiKey: 'YOUR API KEY',
  authDomain: 'newha-45017.firebaseapp.com',
  projectId: 'newha-45017',
  storageBucket: 'newha-45017.appspot.com',
  messagingSenderId: '429605701582',
  appId: '1:429605701582:web:e16b10d62a78ae0b61cd9f',
  measurementId: 'G-9Y28DR5G6W',
});
// Add a rule.
extend('secret', {
  validate: (value) => value === 'example',
  message: 'This is not the magic word',
});

// Register it globally
Vue.component('ValidationProvider', ValidationProvider);

Vue.config.productionTip = false;

window.Kakao.init('YOUR JAVASCRIPT KEY');

new Vue({
  router,
  store,
  vuetify,
  render: (h) => h(App),
}).$mount('#app');
