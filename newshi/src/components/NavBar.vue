<template>
  <div>
    <v-app-bar flat app style="border-bottom:1px solid">
      <v-app-bar-nav-icon
        @click="menu_drawer = !menu_drawer"
      ></v-app-bar-nav-icon>
      <v-spacer />
      <a href="/">
        <div v-if="this.switchTheme == 'true'">
          <v-img contain src="@/assets/images/logo_darkmode.png"></v-img>
        </div>
        <div v-else>
          <v-img contain src="@/assets/images/logo_lightmode.png"></v-img>
        </div>
      </a>
      <v-spacer />
      <v-icon @click="toSearch">mdi-magnify</v-icon>
      <v-dialog
        style="position: fixed"
        v-model="dialog"
        @keydown.esc="dialog = false"
        width="600"
        v-if="!logged"
        :fullscreen="isFull"
        transition="dialog-top-transition"
      >
        <template v-slot:activator="{ on: dialog, attrs }">
          <v-tooltip bottom>
            <template v-slot:activator="{ on: tooltip }">
              <v-btn v-bind="attrs" v-on="{ ...tooltip, ...dialog }" icon>
                <v-icon>mdi-account</v-icon>
              </v-btn>
            </template>
            <span>로그인</span>
          </v-tooltip>
        </template>
        <v-card height="435px">
        <Login
          v-if="isLogin"
          @closeDialog="closeDialog"
          @changeJoin="changeJoin"
          @login="getLogged"
        ></Login>
        <Join
          v-else
          :isKakao="isKakao"
          :user="info"
          @closeDialog="closeDialog"
          @changeLogin="changeLogin"
          @changeKakao="changeKakao"
          @login="getLogged"
        ></Join>
        </v-card>
      </v-dialog>
      <v-menu :open-on-hover="isClicked" offset-y v-else>
        <template v-slot:activator="{ on, attrs }">
          <v-btn v-bind="attrs" v-on="on" icon @click="isClicked = !isClicked">
            <v-icon>mdi-account</v-icon>
          </v-btn>
        </template>
        <v-list>
          <v-list-item>
            <v-list-item-title
              ><v-btn text @click="myPage">마이페이지</v-btn></v-list-item-title
            >
          </v-list-item>
          <v-list-item>
            <v-list-item-title
              ><v-btn text @click="loggedOut"
                >로그아웃</v-btn
              ></v-list-item-title
            >
          </v-list-item>
        </v-list>
      </v-menu>
    </v-app-bar>
    
    <!-- Footer Start -->
    <template>
      <div class="overflow-hidden">
        <v-bottom-navigation
          v-model="value"
          v-show="this.logged == false && this.close != 'true'"
          color="#ff9800"
          fixed
          bottom
        >
          <v-btn @click="dialog = !dialog">
            <span>로그인/회원가입</span>
            <v-icon>mdi-account-plus-outline</v-icon>
          </v-btn>

          <v-btn @click="closeFooter()">
            <span>닫기</span>
            <v-icon>mdi-close-outline</v-icon>
          </v-btn>
        </v-bottom-navigation>
      </div>
    </template>
    <!-- Footer end -->
    <v-navigation-drawer v-model="menu_drawer" fixed temporary>
      <v-list>
        <v-list-item-group>
          <v-list-item v-if="logged" @click="toChannel">
            <v-list-item-avatar>
              <v-img :src="member.thumbnail_path"></v-img>
            </v-list-item-avatar>
            <v-list-item-content>
              <v-list-item-title>{{ member.name }}</v-list-item-title>
              <v-list-item-subtitle>{{ member.id }}</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
          <v-list-item v-else>
            <v-list-item-avatar>
              <v-icon>mdi-account-outline</v-icon>
            </v-list-item-avatar>
            <v-list-item-content>
              <v-list-item-title>로그인이 필요합니다</v-list-item-title>
              <v-list-item-subtitle>로그인 해주세요</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
          <v-divider />
          <v-list-item v-for="(menu, index) in menus" :key="index">
            <router-link :to="menu.router">
              <v-list-item-icon>
                <v-icon>mdi-{{ menu.icon }}</v-icon>
              </v-list-item-icon>
            </router-link>
            <router-link :to="menu.router">
              <v-list-item-title>
                {{ menu.title }}
              </v-list-item-title>
            </router-link>
            <v-switch
              v-if="menu.title === '다크모드'"
              v-model="switchTheme"
              value="true"
              @click="changeTheme()"
              inset
              dense
              color="orange"
              class="mx-3"
            ></v-switch>
          </v-list-item>
        </v-list-item-group>
      </v-list>
    </v-navigation-drawer>
  </div>
</template>
<script>
import Login from '@/components/Login.vue';
import Join from '@/components/Join.vue';
import { mapActions } from 'vuex';
const localThemeMode = localStorage.getItem('themeMode');

export default {
  components: {
    Login,
    Join,
  },
  data() {
    return {
      menu_drawer: false,
      member: {
        name: '김재성',
        id: 'kimjea23@naver.com',
        thumbnail_path: 'https://cdn.vuetifyjs.com/images/lists/1.jpg',
      },
      autocomp_value: [],
      menus: [
        {
          icon: 'bookmark-outline',
          title: '나중에 볼 기사',
          router: '/save',
        },
        { icon: 'brightness-6', title: '다크모드', router: '' },
        {
          icon: 'email-open-outline',
          title: '피드백 보내기',
          router: '/feedback',
        },
        {
          icon: 'comment-processing-outline',
          title: '댓글 운영 정책',
          router: '/policy',
        },
        { icon: 'home-outline', title: '만든이들', router: '/whoweare' },
      ],
      mounted_flag: false,
      dialog: null,
      isLogin: true,
      isKakao: false,
      info: {},
      logged: true,
      switchTheme: '',
      items: ['default', 'absolute', 'fixed'],
      padless: false,
      variant: 'fixed',
      close: '',
      value: 1,
      alert: true,
      isClicked: false,
    };
  },
  computed: {
    getMember() {
      return this.$store.state.userProfile;
    },
    isFull() {
      let check = false;
      switch (this.$vuetify.breakpoint.name) {
        case 'xs':
          check = true;
          break;
        case 'sm':
          check = true;
          break;
        case 'md':
          check = false;
          break;
        case 'lg':
          check = false;
          break;
        case 'xl':
          check = false;
          break;
      }
      return check;
    },
  },
  methods: {
    ...mapActions(['logout', 'getUserInfo']),
    toChannel() {
      this.$router.push('/channel/' + this.member.id);
    },
    toSearch() {
      this.$router.push('/search');
    },
    closeDialog() {
      this.dialog = !this.dialog;
      this.isLogin = true;
    },
    changeJoin() {
      this.isLogin = false;
    },
    changeLogin() {
      this.isLogin = true;
    },
    changeKakao: function(info) {
      this.info = info;
      this.isLogin = false;
      this.isKakao = true;
    },
    myPage() {
      this.$router.push('/mypage');
    },
    changeTheme() {
      // true일 때 darkmode, false일 때 lightmode
      this.$store.dispatch('getThemeMode', this.switchTheme);
      this.$vuetify.theme.dark = this.switchTheme;
    },
    getLogged() {
      this.logged = true;
      this.getUserInfo();
      this.member = this.$store.getters.userProfile;
      this.dialog = !this.dialog;
      this.isLogin = true;
      window.location.reload();
    },
    loggedOut() {
      this.logged = false;
      this.logout();
      this.member = {};
      this.$router.push('/');
    },
    closeFooter() {
      localStorage.setItem('closeFooter', true);
      this.close = 'true';
    },
  },
  watch: {
    getMember: function(val) {
      this.member = val;
    },
  },
  created() {
    if (
      localStorage.getItem('access-token') === null ||
      localStorage.getItem('access-token') === '' ||
      localStorage['access-token'] === undefined
    ) {
      this.logged = false;
      this.member = {};
    } else {
      this.logged = true;
      this.member = this.$store.getters.userProfile;
      if (this.member === null || this.member.id == undefined) {
        this.getUserInfo();
      }
    }
    this.switchTheme = localThemeMode;
    if (localThemeMode == null) this.$vuetify.theme.dark = false;
    else
      localThemeMode.toString() == 'true'
        ? (this.$vuetify.theme.dark = true)
        : (this.$vuetify.theme.dark = false); // 시작하자마자 다크테마인지 아닌지 체크
    this.close = localStorage.getItem('closeFooter');
  },
};
</script>

<style>
#switch {
  display: inline;
}
.theme--dark.v-app-bar.v-toolbar.v-sheet {
  background-color: #1e1e1e !important;
}
.theme--dark.v-navigation-drawer {
  background-color: #252525 !important;
}
.theme--light.v-app-bar.v-toolbar.v-sheet {
  background-color: #ffffff !important;
}
.v-application a {
  text-decoration: none;
  color: inherit !important;
}
.v-list-item__icon {
  margin-left: 0px !important;
}
.v-footer {
  padding: 0 !important;
}
</style>
