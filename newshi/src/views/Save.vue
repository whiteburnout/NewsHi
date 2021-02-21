<template>
  <v-container>
    <h3 class="mt-1">내일 볼 기사</h3>
    <div v-if="this.logged !== null">
      <v-divider></v-divider>
      <!-- <List :news-items="newsItems" /> -->
      <v-list v-if="newsItems.length !== 0">
        <template v-for="(newsInfo, i) in newsItems">
          <v-list-item
            class="main-list"
            :key="newsInfo + i"
            @click="move(newsInfo)"
          >
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
      <div v-else>
        <v-alert
          v-model="alert"
          type="warning"
          border="bottom"
          elevation="4"
          colored-border
          icon="mdi-alert-octagon-outline"
          fixed
        >
        <span>아직 저장한 뉴스가 없습니다.</span>
        <span><v-icon>mdi-bookmark-outline</v-icon>버튼을 눌러 뉴스를 저장해보세요!</span>
          
        </v-alert>
      </div>
    </div>
    <div v-else>
      <Alert />
    </div>
  </v-container>
</template>

<script>
import Alert from '@/components/Alert.vue';
import axios from 'axios';
import { API_BASE_URL } from '@/config';
const API_URL = API_BASE_URL;
const id = localStorage.getItem('id');
const isLogged = localStorage.getItem('access-token');

export default {
  name: 'Save',
  components: { Alert },
  data() {
    return {
      newsItems: [],
      logged: isLogged,
    };
  },
  methods: {
    getSaveList: function() {
      axios
        .get(`${API_URL}` + 'article/savelist' + `?id=${id}`)
        .then((res) => {
          this.newsItems = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    move(newsInfo) {
      this.$router.push({
        name: 'Article',
        params: { newsNo: newsInfo.newsNo, newsInfo2: newsInfo },
      });
    },
  },
  created: function() {
    this.getSaveList();
  },
};
</script>

<style></style>
