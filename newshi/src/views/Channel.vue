<template>
  <div>
    <v-container>
      <v-row no-gutters class="mx-3">
        <v-col class="d-flex justify-center align-center" cols="3">
          <v-responsive :aspect-ratio="1 / 1">
            <v-avatar width="100%" height="100%">
              <v-img :src="curator.thumbnail_path" aspect-ratio="1/1"></v-img>
            </v-avatar>
          </v-responsive>
        </v-col>
        <v-col cols="9">
          <v-container fill-height class="d-flex align-center ml-1">
            <v-row>
              <v-col>
                <div>
                  <h2>{{ curator.name }}</h2>
                </div>
                <div>{{ curator.id }}</div>
              </v-col>
            </v-row>
          </v-container>
        </v-col>
        <v-col cols="4"></v-col>
      </v-row>
      <v-row no-gutters>
        <v-col class="d-flex justify-center">
          <v-container>
            <v-row v-if="isMyPage == true">
              <v-col><v-btn block @click="toMyPage">마이페이지</v-btn></v-col>
              <v-col>
                <v-btn block @click="goBoardScrap">{{ btn_name }}</v-btn>
              </v-col>
            </v-row>
            <v-row v-else>
              <v-col v-if="isSubs == false">
                <v-btn block @click="follow">구독하기</v-btn></v-col
              >
              <v-col v-else>
                <v-btn block @click="unfollow" class="error"
                  >구독취소</v-btn
                ></v-col
              >
              <v-col>
                <v-btn block @click="goBoardScrap">{{ btn_name }}</v-btn>
              </v-col>
            </v-row>
          </v-container>
        </v-col>
      </v-row>
      <v-row no-gutters class="mx-3">
        <v-col>
          <v-card height="600px" style="overflow-y: scroll">
            <router-view :key="$route.fullPath"></router-view>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import axios from 'axios';
import { API_BASE_URL } from '@/config';

export default {
  computed: {
    getMember() {
      return this.$store.state.userProfile;
    },
  },
  watch: {
    getMember: function(val) {
      this.member = val;
    },
    member: function() {
      if (this.member.id == this.$route.params.id) {
        this.isMyPage = true;
      }
      this.subsCheck();
    },
  },
  data() {
    return {
      member: {},
      isMyPage: false,
      isSubs: false,
      curator: {},
      btn_name: '',
    };
  },
  methods: {
    toMyPage() {
      this.$router.push('/mypage');
    },
    subsCheck() {
      axios
        .get(API_BASE_URL + 'subscribe', {
          params: { id: this.member.id },
        })
        .then((response) => {
          // 응답 처리
          var followers = response.data;
          followers.forEach((follower) => {
            if (follower.id == this.$route.params.id) {
              this.isSubs = true;
            }
          });
        })
        .catch((error) => {
          console.log(error);
        });
    },
    showScrap() {
      this.$router.push({
        name: 'Scrap',
        params: { propsIsMyPage: this.isMyPage },
      });
    },
    follow() {
      var frm = new FormData();
      frm.append('id', this.member.id);
      frm.append('id2', this.$route.params.id);
      axios
        .post(API_BASE_URL + 'subsc', frm, {
          headers: { 'Content-Type': 'multipart/form-data' },
        })
        .then(() => {
          window.location.reload();
        });
    },
    unfollow() {
      var frm = new FormData();
      frm.append('id', this.member.id);
      frm.append('id2', this.$route.params.id);
      axios
        .post(API_BASE_URL + 'subscdelete', frm, {
          headers: { 'Content-Type': 'multipart/form-data' },
        })
        .then(() => {
          window.location.reload();
        });
    },
    goBoardScrap() {
      if (this.$router.currentRoute.name == 'ChannelBoard') {
        this.btn_name = '커뮤니티';
        this.$router.push('/channel/' + this.$route.params.id);
      } else {
        this.btn_name = '포스트 페이지';
        this.$router.push('/channel/' + this.$route.params.id + '/board');
      }
    },
  },
  created() {
    if (this.$router.currentRoute.name == 'ChannelBoard') {
      this.btn_name = '포스트 페이지';
    } else {
      this.btn_name = '커뮤니티';
    }

    if (this.$store.getters.userProfile.id != undefined) {
      this.member = this.$store.getters.userProfile;
    }
      // 내 정보랑 같은지 체크해서
      // 하위 컴포넌트에 전달
      // this.showScrap();
      axios.get(API_BASE_URL + 'sidebarUser', 
            { params: { id: this.$route.params.id } },
          ).then((response) => {
            let curator = response.data;
            if(curator.thumbnail_path == null) {
              curator.thumbnail_path = require('@/assets/images/default_avatar.png');
            }
            this.curator = response.data;
            this.curator.id = this.$route.params.id;
          });

    },
}
</script>

<style scoped>
.scrap_dark {
  background-color: black;
  opacity: 0.4;
  width: 100%;
  height: 100%;
}
.ellipsis {
  width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  text-align: center;
}
</style>
