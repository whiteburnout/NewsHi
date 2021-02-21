import { createInstance } from './index.js';
// import 'url-search-params-polyfill';

const instance = createInstance();
// const config = {
//   headers: { "access-token": localStorage.getItem("access-token") }
// };

function login(info, success, fail) {
  instance.defaults.headers['access-token'] = window.localStorage.getItem(
    'access-token'
  );
  const user = {
    id: info.id,
    name: null,
    password: info.password,
    thumbnail_path: null,
    platformType: null,
  };

  instance
    .post('user/login', user)
    .then(success)
    .catch(fail);
}

function socialLogin(info, success, fail) {
  instance.defaults.headers['access-token'] = window.localStorage.getItem(
    'access-token'
  );
  const user = {
    name: info.name,
    password: null,
    thumbnail_path: info.thumbnail_path,
    id: info.id,
    platformType: info.platform_type,
  };

  instance
    .post('user/socialLogin', user)
    .then(success)
    .catch(fail);
}

function join(info, success, fail) {
  instance.defaults.headers['access-token'] = window.localStorage.getItem(
    'access-token'
  );
  // var params = new URLSearchParams();
  // params.append('tag', tag);
  const list = [
    {
      id: info.id,
      name: info.name,
      password: info.password,
      thumbnail_path: info.thumbnail_path,
      platform_type: null,
    },
    {
      tag: info.tags,
    },
  ];

  instance
    .post('join', list)
    .then(success)
    .catch(fail);
}

function emailTest(id, success, fail) {
  instance.defaults.headers['access-token'] = window.localStorage.getItem(
    'access-token'
  );

  instance
    .get('idcheck', {
      params: {
        id: id,
      },
    })
    .then(success)
    .catch(fail);
}

function emailValidTest(id, success, fail) {
  instance.defaults.headers['access-token'] = window.localStorage.getItem(
    'access-token'
  );

  instance
    .get('emailauth', {
      params: {
        id: id,
      },
    })
    .then(success)
    .catch(fail);
}

function nameTest(name, success, fail) {
  instance.defaults.headers['access-token'] = window.localStorage.getItem(
    'access-token'
  );

  instance
    .get('namecheck', {
      params: {
        name: name,
      },
    })
    .then(success)
    .catch(fail);
}

function getInfo(id, success, fail) {
  instance.defaults.headers['access-token'] = window.localStorage.getItem(
    'access-token'
  );

  instance
    .get('user', {
      params: {
        id: id,
      },
    })
    .then(success)
    .catch(fail);
}

function changePassword(info, success, fail) {
  var frm = new FormData();
  frm.append('id', info.id);
  frm.append('password', info.password);

  instance
    .put('changePassword', frm)
    .then(success)
    .catch(fail);
}

function sidebarUser(id, success, fail) {
  instance.defaults.headers['access-token'] = window.localStorage.getItem(
    'access-token'
  );
  instance
    .get('sidebarUser', {
      params: {
        id: id,
      },
    })
    .then(success)
    .catch(fail);
}

function userInfo(userNo, success, fail) {
  instance.defaults.headers['access-token'] = window.localStorage.getItem(
    'access-token'
  );

  instance
    .get('userInfo', {
      params: {
        userNo: userNo,
      },
    })
    .then(success)
    .catch(fail);
}

export {
  login,
  join,
  emailTest,
  emailValidTest,
  nameTest,
  getInfo,
  userInfo,
  socialLogin,
  changePassword,
  sidebarUser,
};
