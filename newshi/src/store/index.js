import Vue from 'vue';
import Vuex from 'vuex';
import router from '@/router';
import { getInfo } from '@/api/user.js';
Vue.use(Vuex);

const store = new Vuex.Store({
  state: {
    userProfile: {},
    themeMode: localStorage.themeMode,
  },
  getters: {
    userProfile: ({ userProfile }) => userProfile,
    loggedIn: ({ loggedIn }) => loggedIn,
    themeMode: ({ themeMode }) => themeMode,
  },
  mutations: {
    SET_USER(state, userProfile) {
      state.userProfile = userProfile;
    },
    SET_THEME_MODE(state, themeMode) {
      state.themeMode = themeMode;
    },
  },
  actions: {
    getThemeMode(context, mode) {
      store.commit('SET_THEME_MODE', mode);
      localStorage.setItem('themeMode', mode);
    },
    getUserInfo({ commit }) {
      if (localStorage['access-token'] && localStorage['access-token'] !== '') {
        getInfo(
          localStorage.id,
          (response) => {
            if (response.data.message === 'success') {
              let user = response.data['userInfo'];
              if (user.thumbnail_path == null) {
                user.thumbnail_path =
                  'https://newha.s3.us-east-2.amazonaws.com/default-avatar.png';
              }
              commit('SET_USER', user);
            } else {
              this.isLoginError = true;

              localStorage.clear();
              commit('SET_USER', null);
              router.push({ name: 'Main' });
            }
          },
          (error) => {
            console.error(error);
            alert('로그인 유지기간이 만료되었습니다. 다시 로그인해주세요');

            localStorage.clear();
            commit('SET_USER', null);
            router.push({ name: 'Main' });
          }
        );
      }
    },
    logout({ commit }) {
      localStorage.clear();
      commit('SET_USER', null);
    },
  },
  modules: {},
});

export default store;
