<template>
  <div>
    <v-container>
        <v-row>
            <v-col cols="3">
                <v-responsive :aspect-ratio="1/1" @click="toChannel(curator.id)" style="cursor: pointer">
                    <v-avatar width=100% height=100%>
                        <v-img v-if="mode=='curator'" :src="curator.thumbnail_path" aspect-ratio="1/1"></v-img>
                        <v-icon v-else size="min(10vw,100px)" color="grey lighten-1">mdi-pound</v-icon>
                    </v-avatar>
                </v-responsive>
            </v-col>
            <v-col cols="3" class="d-flex align-center justify-start" style="padding:0px;">
                <div class="ml-0" style="font-size:min(6vw, 40px); cursor: pointer" @click="toChannel(curator.id)">{{search_word}}</div>
            </v-col>
            <v-col cols="6" v-if="mode == 'curator' && member.id != curator.id" class="d-flex align-center justify-end">
              <v-btn v-if="isSubs == false" @click="follow">구독</v-btn>
              <v-btn v-else @click="unfollow" class="error">구독취소</v-btn>
            </v-col>
            <v-col v-else class="d-flex align-center justify-end">
              <v-btn disabled>구독</v-btn>
            </v-col>
        </v-row>
        <v-row v-for="(news, index) in scrap_news" :key="index">
        <v-col>
          <v-card>
            <v-container>
              <v-row>
                <v-col class="caption">
                  {{news.article_date[0]}}
                </v-col>
              </v-row>
              <v-row no-gutters>
                <v-col class="ma-auto" cols=auto>
                  <v-avatar size="120" tile>
                    <v-img :src="news.image_path[0]"></v-img>
                  </v-avatar>
                </v-col>
                <v-col style="width:0; cursor: pointer" @click="move(news)">
                  <v-card-title>
                    <div class="ellipsis">{{news.title[0]}}</div>
                  </v-card-title>
                  <v-card-subtitle>
                    <div class="ellipsis d-flex justify-left" >{{news.article_bot_summary[0]}}</div>
                  </v-card-subtitle>
                </v-col>
              </v-row>
            </v-container>
          </v-card>
        </v-col>
      </v-row>
      <v-row v-if="scrap_news.length == 0" >
        <v-col class="d-flex justify-center mt-10">
          <h1 style='color:#B9B9B9'>기사가 없습니다.</h1>
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
      this.subsCheck();
    },
  },
    data() {
        return {
            mode: '',
            search_word: '',
            scrap_news: [],
            curator: {},
            member: {},
            isSubs: false,
        }
    },
    methods: {
      move(newsInfo){
        let newsInfo2 = {
          article_bot_summary: newsInfo.article_bot_summary[0],
          article_date: newsInfo.article_date[0],
          article_image_caption: newsInfo.article_image_caption[0],
          company: newsInfo.company[0],
          content: newsInfo.content[0],
          dislike_cnt: newsInfo.dislike_cnt[0],
          image_path: newsInfo.image_path[0],
          like_cnt: newsInfo.like_cnt[0],
          subtitle: newsInfo.subtitle[0],
          title: newsInfo.title[0],
          url: newsInfo.url[0]
        }
        this.$router.push({name: 'Article', params: {newsNo: newsInfo2.newsNo, newsInfo2: newsInfo2} })
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
              if (follower.id == this.curator.id) {
                this.isSubs = true;
              }
            });
          })
          .catch((error) => {
            console.log(error);
          });
      },
      follow() {
        if (localStorage['access-token'] && localStorage['access-token'] !== '') {
          var frm = new FormData();
          frm.append('id', this.member.id);
          frm.append('id2', this.curator.id);
          axios
            .post(API_BASE_URL + 'subsc', frm, {
              headers: { 'Content-Type': 'multipart/form-data' },
            })
            .then(() => {
              window.location.reload();
            });
        }
      },
      unfollow() {
        if (localStorage['access-token'] && localStorage['access-token'] !== '') {
        var frm = new FormData();
        frm.append('id', this.member.id);
        frm.append('id2', this.curator.id);
        axios
          .post(API_BASE_URL + 'subscdelete', frm, {
            headers: { 'Content-Type': 'multipart/form-data' },
          })
          .then(() => {
            window.location.reload();
          });
        }
      },
      numberWithCommas(x) {
      return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
      },
      toChannel(id) {
        this.$router.push('/channel/' + id);
      },
    },
    
    created() {
        this.mode = this.$route.params.mode;
        this.search_word = this.$route.params.search_word;

        if(this.$store.getters.userProfile.id != undefined) {
          this.member = this.$store.getters.userProfile;
        }

        if(this.$route.params.mode == 'curator') {
          let name = this.$route.params.search_word;
          //이름으로 아이디랑 썸네일 받아오기
          axios.get(API_BASE_URL + 'search/people', 
            { params: { keyword: name } },
            ).then((response) => { 
                let resData = response.data;
                let arr = [];
                resData.forEach(e => {
                  if(e['name'] == name){
                    if(e.thumbnail_path == null){
                      e.thumbnail_path = require('@/assets/images/default_avatar.png');
                    }
                    this.curator = e;
                  }
                });
                this.autocomp_value = arr;
            })
          //이름으로 기사들 받아오기
          axios.get(API_BASE_URL + 'article/newsByName', 
            { params: { name: name } },
            ).then((response) => { 
                this.scrap_news = response.data;
                for(let i = 0; i < this.scrap_news.length; i++) {
                  if(this.scrap_news[i].image_path[0] == null) {
                    this.scrap_news[i].image_path[0] = require('@/assets/images/default-image.jpg');
                  }
                }
            })
        } else {
          let tag = this.$route.params.search_word;
          axios.get(API_BASE_URL + 'article/newsByTag', 
            { params: { tagName: tag } },
            ).then((response) => { 
                this.scrap_news = response.data;
                for(let i = 0; i < this.scrap_news.length; i++) {
                  if(this.scrap_news[i].image_path[0] == null) {
                    this.scrap_news[i].image_path[0] = require('@/assets/images/default-image.jpg');
                  }
                }
            });
        }
    },
}
</script>

<style>

</style>