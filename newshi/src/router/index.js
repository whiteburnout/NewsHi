import Vue from 'vue';
import VueRouter from 'vue-router';
import Article from '../views/Article.vue';
import Main from '../views/Main.vue';
import MyPage from '../views/MyPage.vue';
import FindPw from '../views/FindPw.vue';
import ChangePw from '../views/ChangePw.vue';
import Feedback from '../views/Feedback.vue';
import Save from '../views/Save.vue';
import Policy from '../views/Policy.vue';
import Whoweare from '../views/Whoweare.vue';
import MyInfo from '../components/mypage/MyInfo';
import Channel from '../views/Channel.vue';
import Scrap from '../components/channel/Scrap.vue';
import ScrapNews from '../components/channel/ScrapNews.vue';
// import ToScrap from '../components/channel/ToScrap.vue';
import ModifyMyinfo from '../components/mypage/ModifyMyinfo.vue';
import Search from '../views/Search.vue';
import NoSearch from '../components/search/NoSearch.vue';
import SearchResult from '../components/search/SearchResult.vue';
import BoardDetail from '../views/BoardDetail.vue';
import AddBoard from '../views/AddBoard.vue';
import ModifyBoard from '../views/ModifyBoard.vue';
import Link from '../views/Link.vue';
import NotFound from '../views/404.vue';
import ChannelBoard from '../components/channel/ChannelBoard.vue';

Vue.use(VueRouter);

const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(() => {
    return window.location.reload();
  });
};

const requireAuth = () => (to, from, next) => {
  if (localStorage['access-token'] && localStorage['access-token'] !== '') {
    return next();
  } else {
    return next('/');
  }
};

const routes = [
  {
    path: '/404',
    name: 'notFound',
    component: NotFound,
  },
  {
    path: '*',
    redirect: '/404',
  },
  {
    path: '/article/:newsNo',
    name: 'Article',
    component: Article,
    props: true,
  },
  {
    path: '/findpw',
    name: 'FindPw',
    component: FindPw,
  },
  {
    path: '/changepw/:id',
    name: 'ChangePw',
    component: ChangePw,
    props: true,
  },
  {
    path: '/search',
    name: 'Search',
    component: Search,
    children: [
      {
        path: '',
        name: 'NoSearch',
        component: NoSearch,
      },
      {
        path: ':mode/:search_word',
        name: 'SearchResult',
        component: SearchResult,
      },
    ],
  },
  {
    path: '/',
    name: 'Main',
    component: Main,
  },
  {
    path: '/feedback',
    name: 'Feedback',
    component: Feedback,
  },
  {
    path: '/save',
    name: 'Save',
    component: Save,
  },
  {
    path: '/policy',
    name: 'Policy',
    component: Policy,
  },
  {
    path: '/whoweare',
    name: 'Whoweare',
    component: Whoweare,
  },
  {
    path: '/mypage',
    name: 'MyPage',
    component: MyPage,
    beforeEnter: requireAuth(),
    children: [
      {
        path: '/',
        name: 'MyInfo',
        component: MyInfo,
      },
      {
        path: 'modify',
        name: 'ModifyMyinfo',
        component: ModifyMyinfo,
      },
    ],
  },
  {
    path: '/channel/:id',
    name: 'Channel',
    component: Channel,
    children: [
      {
        path: '/',
        name: 'Scrap',
        component: Scrap,
        props: true,
      },
      {
        path: 'scrap/:scrap_no',
        name: 'ScrapNews',
        component: ScrapNews,
      },
      {
        path: 'board',
        name: 'ChannelBoard',
        component: ChannelBoard,
      },
    ],
  },
  {
    path: '/addBoard',
    name: 'AddBoard',
    component: AddBoard,
    beforeEnter: requireAuth(),
  },
  {
    path: '/modifyBoard',
    name: 'ModifyBoard',
    component: ModifyBoard,
    beforeEnter: requireAuth(),
    props: true,
  },
  {
    path: '/boardDetail',
    name: 'BoardDetail',
    component: BoardDetail,
  },
  {
    path: '/link',
    name: 'Link',
    component: Link,
    beforeEnter: requireAuth(),
    props: true,
  },
];

const router = new VueRouter({
  mode: 'history',
  routes,
});

export default router;
