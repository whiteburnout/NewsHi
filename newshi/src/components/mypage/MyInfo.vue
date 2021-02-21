<template>
  <v-container>
    <v-row class="mt-1">
      <v-card
        class="mx-auto"
        max-width="450"
        outlined
      >
        <v-list-item three-line>
          <!-- 프로필 -->
          <v-dialog v-model="dialog">
            <template v-slot:activator="{ on, attrs }">
              <v-avatar class="elevation-13 mr-2" size="110px">
                <v-avatar size="100px" v-bind="attrs" v-on="on">
                  <v-img :src="member.thumbnail_path"></v-img>
                </v-avatar>
              </v-avatar>
            </template>
            <v-card>
              <v-card-title>파일 업로드</v-card-title>
              <v-card-actions>
              <v-file-input v-model="file"></v-file-input>
              </v-card-actions>
              <v-card-actions>
              <v-btn @click="fileUpload">전송하기!</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
          <v-list-item-content class="d-flex">
            <!-- 이름 -->
            <v-list-item-title class="d-flex justify-center mb-1">
              <h3>{{member.name}}</h3>
            </v-list-item-title>
            <v-list-item-subtitle class="d-flex justify-center">
              {{member.id}}
            </v-list-item-subtitle>
            <v-card-actions class="d-flex justify-space-around">
              <v-btn
                rounded
                text
                @click="toChannel(member.id)"
              >
              <v-icon>mdi-post</v-icon>
                커뮤니티
              </v-btn>
              <v-btn
                text
                rounded
                @click="toModify"
              >
              <v-icon>mdi-information</v-icon>
                정보수정
              </v-btn>
            </v-card-actions>
          </v-list-item-content>
        </v-list-item>
      </v-card>
    </v-row>
    <!-- 관심태그 추가 -->
    <v-card-actions class="d-flex justify-center mt-2">
      <v-dialog v-model="dialog2" width="unset">
        <template v-slot:activator="{ on, attrs }">
          <v-btn x-large v-bind="attrs" v-on="on" text>
            <v-icon>mdi-pound</v-icon>
            관심태그 추가하기
          </v-btn>
        </template>
      <v-card >
        <v-card-title>관심태그 추가</v-card-title>
          <v-card-actions>
            <v-chip-group
              v-model="tags"
              active-class="yellow darken-3"
              column
              multiple
            >
              <v-chip border medium v-for="tag in tagName" :key="tag">
                {{ tag }}
              </v-chip>
            </v-chip-group>
          </v-card-actions>
          <v-card-actions>
            <v-btn @click="modifyTags" rounded >수정</v-btn><v-btn @click="dialog2=flase" rounded >취소</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-card-actions> 
    <!-- 태그 나열 -->
      <v-row>
        <v-col class="text-center">
          <div>
            <v-btn v-for="(hashtag, index) in hashtags" 
              :key="index" 
              rounded
              text 
              style="font-size:90%" 
              color="#646464" 
              @click="toSearch(hashtag)"
            >#<u>{{hashtag}}</u></v-btn>
          </div>
        </v-col>
      </v-row>
      <!-- tags end -->
    <v-row>
      <v-col>
    <v-card>
      <v-list style="text-align: left">
          <v-subheader>구독중인 큐레이터</v-subheader>
          <v-list-item v-for="(follower, index) in followers" :key="index">
            <v-list-item-avatar @click="toChannel(follower.id)" style="cursor: pointer">
              <v-img :src="follower.thumbnail_path"></v-img>
            </v-list-item-avatar>
            <v-list-item-content @click="toChannel(follower.id)" style="cursor: pointer">
              <v-list-item-title>{{follower.name}}</v-list-item-title>
              <v-list-item-subtitle>{{follower.id}}</v-list-item-subtitle>
            </v-list-item-content>
            <v-list-item-action>
              <v-btn color="secondary" 
              dark 
              rounded
              @click="unfollow(follower.id)">구독 취소</v-btn></v-list-item-action>
          </v-list-item>
      </v-list>
    </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import axios from 'axios';
import { mapActions } from 'vuex';
import { uploadImage } from '@/api/board.js';
import { API_BASE_URL } from '@/config';

const tag_dict = {
        '속보': 0,
        '정치': 1,
        '경제': 2,
        '사회': 3,
        '생활/문화': 4,
        '세계/국제': 5,
        'IT/과학': 6,
        '오피니언': 7,
        0: '속보',
        1: '정치',
        2: '경제',
        3: '사회',
        4: '생활/문화',
        5: '세계/국제',
        6: 'IT/과학',
        7: '오피니언',
}

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
      this.getFollowers();
      this.getTagList();
    }
  },
  methods: {
    ...mapActions(['logout', 'getUserInfo']),
    userDelete() {
      axios.delete(API_BASE_URL + 'delete', {params: { id: this.member.id }})
      .then(() => {
        this.logout();
        this.$router.go(this.$router.currentRoute);
        // this.$router.push('/');
      })
    },
    toModify() {
      this.$router.push('mypage/modify');
    },
    toSearch(tag) {
      tag = tag.replace('/', '%2F');
      this.$router.push('/search/hashtag/' + tag);
    },
    unfollow(id) {
      var frm = new FormData();
      frm.append("id", this.member.id);
      frm.append("id2", id);
      
      axios.post(API_BASE_URL + 'subscdelete', frm, { headers: { 'Content-Type': 'multipart/form-data' }})
      .then(() => {
        this.$router.go(this.$router.currentRoute);
      })
    },
    modifyTags(){
      var params = new URLSearchParams();
      params.append("id", this.member.id);
      this.tags.forEach(function(tag) {
        params.append("list",tag_dict[tag]);
      });
      axios.get(API_BASE_URL + 'tagListUpdate', 
      { params: params },
    ).then(() => { 
      this.$router.go(this.$router.currentRoute);
      }) .catch((error) => { 
        // 예외 처리 
        console.log(error);
      })
    },
    toChannel(curator) {
      this.$router.push('channel/' + curator);
    },
    fileUpload() {
      let file = this.file;
      const fileName = file.name;
      uploadImage(
        file,
        (response) => {
          if (response.data.message === 'success') {
            this.imageSrc =
              'https://newha.s3.us-east-2.amazonaws.com/' + fileName;
            var frm = new FormData();
            frm.append("id", this.member.id);
            frm.append("thumbnail_path", this.imageSrc);
            axios.post(API_BASE_URL + 'upload', frm, { headers: { 'Content-Type': 'multipart/form-data' }})
            .then(() => {
              this.$router.go(this.$router.currentRoute);
            })
          } else {
            alert('큐레이터의 데이터를 받아오는데 실패했습니다.');
          }
        },
        (error) => {
          console.error(error);
          alert('큐레이터의 데이터를 받아오는 중 에러가 발생했습니다.');
        }
      );
    },
    getFollowers() {
      axios.get(API_BASE_URL + 'subscribe', 
      { params: { id: this.member.id } },
    ).then((response) => { 
        // 응답 처리 
        var followers = response.data;
        followers.forEach(function(follower) {
          if(follower.thumbnail_path == null) {
            follower.thumbnail_path = require('@/assets/images/default_avatar.png');
          }
        })
        this.followers = followers;
      }) .catch((error) => { 
        // 예외 처리 
        console.log(error);
      })
    },
    getTagList() {
      axios.get(API_BASE_URL + 'tagList', 
      { params: { id: this.member.id } },
    ).then((response) => { 
      var hashtags = response.data;
      hashtags.forEach((hashtag) => {
        this.hashtags.push(hashtag['name'])
        this.tags.push(tag_dict[hashtag['name']]);
      })
      })
      
    },
    mouseover() {
      alert('Hello')
    }
  },
  data() {
    return {
      member: {},
      hashtags: [],
      followers: [],
      file: [],
      dialog: false,
      dialog2: false,
      tagName: [
        '속보',
        '정치',
        '경제',
        '사회',
        '생활/문화',
        '세계/국제',
        'IT/과학',
        '오피니언',
      ],
      tags:[],
      show: false,
    }
  },
  created() {
    if(this.$store.getters.userProfile.id != undefined) {
      this.member = this.$store.getters.userProfile;
    }
    // 내 hashtags 불러오는 axios(임시)
    // this.hashtags = ['#속보', '#정치', '#경제', '#사회', '#문화'];
    
  },
}
</script>

<style>
.v-list-item--three-line .v-list-item__content{
  align-self: auto !important;
}
</style>