<template>
  <v-container>
    <v-row class="main-news" v-if="this.isLoggedIn == true">
      <v-sheet class="mx-auto" max-width="800">
        <v-slide-group v-model="model" 
          center-active id="recommend"
          class="v-slide-group-maxvw"
        >
          <v-slide-item v-for="(user, idx) in users" :key="idx">
            <v-card
              class="d-flex flex-row ma-2"
              max-width="200px"
              min-width="160px"
              elevation="3"
            >
              <v-list-item-content class="justify-center pa-2">
                <div class="mx-auto text-center">
                  <div @click="toChannel(user.id[0])">
                    <v-avatar color="brown" class="elevation-5">
                      <v-img
                        v-if="user.thumnail_path !== undefined"
                        :src="user.thumnail_path[0]"
                      ></v-img>
                    </v-avatar>
                    <h3 v-if="user.name !== undefined">{{ user.name[0] }}</h3>
                    <span
                      class="caption mt-1"
                      v-for="tag in user.tag"
                      :key="tag"
                    >
                      #{{ tag }}
                    </span>
                    <br />
                  </div>
                  <v-btn
                    class="mt-1"
                    rounded
                    color="#fcbf49"
                    dark
                    v-if="user.id !== undefined"
                    @click="follow(user.id[0])"
                    elevation="3"
                  >
                    구독
                  </v-btn>
                </div>
              </v-list-item-content>
            </v-card>
          </v-slide-item>
        </v-slide-group>
      </v-sheet>
      <NewsList :news-infos="newsInfos" />
      <div v-if="this.subscribers.length == 0" class="mx-auto my-2">
        <v-alert
          v-model="alert"
          dismissible
          color="#fcbf49"
          border="bottom"
          elevation="4"
          colored-border
          icon="mdi-cursor-pointer"
          fixed
        >
          추천 받은 큐레이터를 구독해보세요
        </v-alert>
      </div>
    </v-row>

    <v-row class="main-news" v-else>
      <v-list>
        <template v-for="(newsInfo, i) in basicNews">
          <v-list-item :key="newsInfo + i" @click="move(newsInfo)">
            <v-list-item-avatar rounded>
              <v-img :src="newsInfo.image_path" centered></v-img>
            </v-list-item-avatar>
            <v-list-item-content>
              <v-list-item-title v-html="newsInfo.title"></v-list-item-title>
              <v-list-item-subtitle
                v-html="newsInfo.company"
              ></v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
        </template>
      </v-list>
    </v-row>
  </v-container>
</template>

<script>
import NewsList from '../components/NewsList.vue';
import axios from 'axios';
import { API_BASE_URL } from '@/config';
const API_URL = API_BASE_URL;
const id = localStorage.getItem('id');

export default {
  name: 'Main',
  components: {
    NewsList,
  },
  methods: {
    toChannel(curator) {
      this.$router.push('channel/' + curator);
    },
    getData: function() {
      axios.get(`${API_URL}` + 'subscribe' + `?id=${id}`).then((res) => {
        let subscribes = res.data;
        this.subscribers = subscribes;
        subscribes.forEach((element) => {
          let subscriberId = element.id;
          let newsInfo = {
            name: element.name,
            avatar: element.thumbnail_path,
            newsLists: [],
          };
          axios
            .get(`${API_URL}` + 'article/curatorscrap' + `?id=${subscriberId}`)
            .then((response) => {
              newsInfo.newsLists = response.data;
              this.newsInfos.push(newsInfo);
            })
            .catch((err) => {
              console.log(err);
            });
        });
      });
    },
    isLogged: function() {
      if (
        localStorage.getItem('access-token') === null ||
        localStorage.getItem('access-token') === '' ||
        localStorage['access-token'] === undefined
      ) {
        this.isLoggedIn = false;
        axios
          .get(`${API_URL}` + 'article')
          .then((res) => {
            this.basicNews = res.data;
          })
          .catch((err) => {
            console.log(err);
          });
      } else {
        this.isLoggedIn = true;
      }
    },
    move(newsInfo) {
      this.$router.push({
        name: 'Article',
        params: { newsNo: newsInfo.newsNo, newsInfo2: newsInfo },
      });
    },
    getRecommend() {
      axios
        .get(`${API_URL}` + 'userrecommend' + `?id=${id}`)
        .then((res) => {
          this.users = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    follow(subscribeid) {
      var frm = new FormData();
      frm.append('id', id);
      frm.append('id2', subscribeid);
      axios
        .post(`${API_URL}` + 'subsc', frm, {
          headers: { 'Content-Type': 'multipart/form-data' },
        })
        .then(() => {
          this.$router.go(this.$router.currentRoute);
        });
    },
  },
  data: function() {
    return {
      tab: null,
      newsInfos: [],
      isLoggedIn: '',
      basicNews: [],
      users: [],
      model: null,
      subscribers: [],
      alert: true,
    };
  },
  created: function() {
    this.isLogged();
    this.isLoggedIn ? this.getData() : this.isLoggedIn;
    this.isLoggedIn ? this.getRecommend() : this.isLoggedIn;
  },
};
</script>

<style>
.v-card--reveal {
  align-items: center;
  bottom: 0;
  justify-content: center;
  opacity: 0.5;
  position: absolute;
  width: 100%;
}
#recommend .theme--dark.v-card {
  background-color: #222222 !important;
}
.main-news {
  padding-bottom: 50px;
}
.v-list {
  max-width: 100vw !important;
}
.v-slide-group-maxvw{
  max-width: 100vw !important;
}
</style>
