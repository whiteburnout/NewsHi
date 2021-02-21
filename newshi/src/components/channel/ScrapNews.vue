<template>
  <div>
    <v-btn x-large text @click="backPage">
      <v-icon>mdi-arrow-left</v-icon>
    </v-btn>
    <v-container>
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
                <v-col style="width:0">
                  <div @click="move(news)" style="cursor: pointer">
                  <v-card-title>
                    <div class="ellipsis">{{news.title[0]}}</div>
                  </v-card-title>
                  <v-card-subtitle>
                    <div class="ellipsis" v-html="news.curator_summary[0]"></div>
                  </v-card-subtitle>
                  </div>
                  <v-card-actions class="d-flex justify-end">
                    <v-btn dark small :color="news.islike != undefined && news.islike[0] ? 'red':'grey'" @click="likeBtn(news.scrapNo[0], news.islike)"> 
                      <v-icon dark>mdi-heart</v-icon>
                      <div>{{news.like_cnt[0]}}</div>
                    </v-btn>
                    <v-btn dark small :color="news.isdislike != undefined && news.isdislike[0] ? 'blue':'grey'" @click="dislikeBtn(news.scrapNo[0], news.isdislike)"> 
                      <v-icon dark>mdi-heart-broken</v-icon>
                      <div>{{news.dislike_cnt[0]}}</div>
                    </v-btn>
                  </v-card-actions>
                </v-col>
              </v-row>
            </v-container>
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
      if(this.member.id == this.$route.params.id){
        this.isMyPage = true;
      }
    },
  },
  data() {
    return {
      post_no: 0,
      id: String,
      scrap_news: [],
      member: {},
    }
  },
  methods: {
    move(newsInfo){
        let newsInfo2 = {
          article_bot_summary: newsInfo.article_bot_summary[0],
          article_date: newsInfo.article_date[0],
          article_image_catpion: newsInfo.article_image_catpion[0],
          company: newsInfo.company[0],
          content: newsInfo.content[0],
          curator_summary: newsInfo.curator_summary[0],
          dislike_cnt: newsInfo.dislike_cnt[0],
          image_path: newsInfo.image_path[0],
          isdislike: newsInfo.isdislike[0],
          islike: newsInfo.islike[0],
          like_cnt: newsInfo.like_cnt[0],
          new_image: newsInfo.new_image[0],
          newsNo: newsInfo.newsNo[0],
          scrapNo: newsInfo.scrapNo[0],
          subtitle: newsInfo.subtitle[0],
          title: newsInfo.title[0],
          url: newsInfo.url[0]
        }
        this.$router.push({name: 'Article', params: {newsNo: newsInfo2.newsNo, newsInfo2: newsInfo2} })
      },

    getScrap() {
      this.scrap_news = [];
      axios.get(API_BASE_URL + 'article/scraplist', 
            { params: { postNo: this.post_no } },
          ).then((response) => {
            let scrap_news = response.data;
            // 사진없는 기사 이미지 추가할것.
            // 좋아요 싫어요 axios 추가하자
            if (localStorage['access-token'] && localStorage['access-token'] !== '') {
              for(let i = 0; i < scrap_news.length; i++) {
                setTimeout(() => {
                  setTimeout(() => {
                    axios.get(API_BASE_URL + 'article/islike',
                  {params: { id: this.member.id, scrapNo: scrap_news[i].scrapNo[0]}})
                  .then((response) => {
                    if(response.data['message'] == 'success') {
                      scrap_news[i]['islike'] = new Array(true);
                    } else { scrap_news[i]['islike'] = new Array(false); }
                  });
                  }, 10);
                  
                  setTimeout(() => {
                    axios.get(API_BASE_URL + 'article/isdislike',
                  {params: { id: this.member.id, scrapNo: scrap_news[i].scrapNo[0]}})
                  .then((response) => {
                    if(response.data['message'] == 'success') {
                      scrap_news[i]['isdislike'] = new Array(true);
                    } else { scrap_news[i]['isdislike'] = new Array(false); }
                  }).finally(() => { 
                    if(scrap_news[i].image_path[0] == null) scrap_news[i].image_path[0] = require('@/assets/images/default-image.jpg')
                    this.scrap_news.push(scrap_news[i]); 
                  });
                  }, 20);
                }, i * 30);
              }
            } else {
              this.scrap_news = scrap_news;
            }
          });
          
    },
    likeBtn(scrapNo, check) {
      if(check == undefined) {
        alert('로그인을 해주세요.');
        return;
      }
      check = check[0];
      if (localStorage['access-token'] && localStorage['access-token'] !== '') {
        axios.get(API_BASE_URL + 'article/like',
                {params: { id: this.member.id, scrapNo: scrapNo}})
                .then(() => {
                });
        
        if(check == true) {
          axios.get(API_BASE_URL + 'article/decrealike',
                {params: {scrapNo: scrapNo}})
                .then(() => {
                });
        } else {
          axios.get(API_BASE_URL + 'article/increalike',
                {params: {scrapNo: scrapNo}})
                .then(() => {
                });
        }
        setTimeout(() => {
          this.getScrap();
        }, 50);
      }
    },
    dislikeBtn(scrapNo, check) {
      if(check == undefined) {
        alert('로그인을 해주세요.');
        return;
      }
      check = check[0];
      if (localStorage['access-token'] && localStorage['access-token'] !== '') {
        axios.get(API_BASE_URL + 'article/dislike',
                {params: { id: this.member.id, scrapNo: scrapNo}})
                .then(() => {
                });
        if(check == true) {
          axios.get(API_BASE_URL + 'article/decreadislike',
                {params: {scrapNo: scrapNo}})
                .then(() => {
                });
        } else {
          axios.get(API_BASE_URL + 'article/increadislike',
                {params: {scrapNo: scrapNo}})
                .then(() => {
                });
        }
        setTimeout(() => {
          this.getScrap();
        }, 50);
      }
    },
    numberWithCommas(x) {
      return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },
    backPage() {
      this.$router.go(-1);
    }
  },
  created() {
    this.post_no = this.$route.params.scrap_no;
    this.id = this.$route.params.id;
    if(this.$store.getters.userProfile.id != undefined) {
      this.member = this.$store.getters.userProfile;
    }
    this.getScrap();
  },
}
</script>

<style>
  .ellipsis{
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        text-align: center;
    } 
</style>