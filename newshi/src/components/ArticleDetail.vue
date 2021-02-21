<template>
  <v-container>
    <!-- news header -->
    <header>
      <div class="news-title d-flex justify-content-start">
        {{ news.title }}
      </div>
      <div class="news-header text-center">
        {{ news.article_date }}
      </div>
      <div class="news-info d-flex justify-content-start">
        <div class="my-auto">{{ news.company }}</div>
        <div class="my-auto">
          <a :href="news.url">원본보기</a>
        </div>
        <v-spacer></v-spacer>
        <v-btn @click="save()" icon>
          <v-icon medium v-if="this.saved !== true"
            >mdi-bookmark-outline</v-icon
          >
          <v-icon medium v-else color="#ff9800">mdi-bookmark</v-icon>
        </v-btn>
      </div>
      <Alert v-if="this.alert == true" />
      <!-- TTS Play icon -->
      <div class="d-flex justify-content-start">
        <v-row align="center" justify="space-around" class="my-2">
          <v-btn
            @click="tts"
            dark
            rounded
            elevation="3"
            v-if="this.play === false"
          >
            <v-icon>
              mdi-volume-high
            </v-icon>
            Play
          </v-btn>
          <v-btn dark rounded elevation="3" @click="tts" v-else color="#ff9800">
            <v-icon>
              mdi-volume-mute
            </v-icon>
            Stop
          </v-btn>
        </v-row>
      </div>
    </header>
    <!-- main image -->
    <template>
      <div
        v-touch="{
          left: () => swipe('Left'),
          right: () => swipe('Right'),
        }"
      >
        <section>
          <div class="news-image">
            <v-img :src="news.image_path" max-width:="700" max-height="480">
            </v-img>
            <div class="news-image-caption text-center">
              {{ news.news_image_caption }}
            </div>
          </div>
        </section>
        <!-- news body -->
        <section class="my-2" v-if="swipeDirection === 'Right'">
          <div class="news-body-text">
            <div class="article-bot-summary">
              <h3 class="text-center">핵심 요약</h3>
              <p v-html="news.article_bot_summary"></p>
            </div>
            <div class="text-center my-3">
              <h3 class="my-auto">기사 본문</h3>
            </div>
            <div class="article-content">
              <p v-html="news.content"></p>
            </div>
            <div
              class="news-body-text hidden"
              v-if="news.curator_summary != undefined"
            >
              <div class="my-3">
                <h3 class="text-center">큐레이터의 오피니언</h3>
                <p v-html="news.curator_summary"></p>
              </div>
              <div class="d-flex my-2">
                <div class="mx-auto">
                  <v-btn icon class="mx-2" @click="like()">
                    <v-icon large v-if="this.liked === false"
                      >mdi-thumb-up-outline</v-icon
                    >
                    <v-icon large v-else color="#ff9800">mdi-thumb-up</v-icon>
                  </v-btn>
                  <v-btn icon class="mx-2" @click="dislike()">
                    <v-icon large v-if="this.disliked === false"
                      >mdi-thumb-down-outline</v-icon
                    >
                    <v-icon large v-else color="#ff9800">mdi-thumb-down</v-icon>
                  </v-btn>
                </div>
              </div>
              <h3 class="text-center my-3">
                큐레이터의 오피니언이 괜찮았나요?
              </h3>
            </div>
          </div>
        </section>
        <section class="my-2" v-if="swipeDirection === 'Left'">
          <div class="news-body-text">
            <div class="article-bot-summary">
              <h3 class="text-center my-3">큐레이터의 오피니언</h3>
              <p v-html="news.curator_summary"></p>
            </div>
            <div class="d-flex my-4">
              <div class="mx-auto">
                <v-btn icon class="mx-2" @click="like()">
                  <v-icon large v-if="this.liked === false"
                    >mdi-thumb-up-outline</v-icon
                  >
                  <v-icon large v-else color="#ff9800">mdi-thumb-up</v-icon>
                </v-btn>
                <v-btn icon class="mx-2" @click="dislike()">
                  <v-icon large v-if="this.disliked === false"
                    >mdi-thumb-down-outline</v-icon
                  >
                  <v-icon large v-else color="#ff9800">mdi-thumb-down</v-icon>
                </v-btn>
              </div>
            </div>
            <p class="text-center my-3">큐레이터의 오피니언이 괜찮았나요?</p>
          </div>
        </section>
      </div>
    </template>
  </v-container>
</template>

<script>
import Alert from '../components/Alert.vue';
import axios from 'axios';
import { API_BASE_URL } from '@/config';
const API_URL = API_BASE_URL;
const id = localStorage.getItem('id');
const isLogged = localStorage.getItem('access-token');

export default {
  name: 'ArticleDetail',
  components: {
    Alert,
  },
  props: ['newsInfo'],
  methods: {
    tts() {
      this.play = !this.play;
      let voices = [];
      function setVoiceList() {
        voices = window.speechSynthesis.getVoices();
      }
      setVoiceList();
      if (window.speechSynthesis.onvoiceschanged !== undefined) {
        window.speechSynthesis.onvoiceschanged = setVoiceList;
      }
      function speech(txt, play) {
        if (!window.speechSynthesis) {
          alert(
            '음성 재생을 지원하지 않는 브라우저입니다. 크롬, 파이어폭스 등의 최신 브라우저를 이용하세요'
          );
          return;
        }
        const lang = 'ko-KR';
        let utterThis = new SpeechSynthesisUtterance(txt);
        utterThis.onend = function() {};
        utterThis.onerror = function(event) {
          console.log('error', event);
        };
        for (let i = 0; i < voices.length; i++) {
          if (
            voices[i].lang.indexOf(lang) >= 0 ||
            voices[i].lang.indexOf(lang.replace('-', '_')) >= 0
          ) {
            utterThis.voice = voices[i];
          }
        }
        utterThis.lang = lang;
        utterThis.pitch = 1;
        utterThis.rate = 1; //속도
        if (play === true) {
          window.speechSynthesis.speak(utterThis);
        } else {
          window.speechSynthesis.cancel();
        }
      }
      let t = this.news.content;
      speech(t, this.play);
    },
    swipe(direction) {
      if (this.news.curator_summary != undefined) {
        this.swipeDirection = direction;
      }
    },
    save() {
      if (this.logged !== null) {
        this.saved = !this.saved;
        const scrapNo = this.newsInfo.scrapNo;
        axios
          .get(
            `${API_URL}` + 'article/save' + `?id=${id}` + `&scrapNo=${scrapNo}`
          )
          .then((res) => {
            if (res.data.message == 'success' && this.saved == true) {
              alert('기사가 성공적으로 저장되었습니다.');
            }
          });
      } else {
        this.alert = !this.alert;
      }
    },
    saveCheck() {
      if (this.logged !== null) {
        const newsNo = this.newsInfo.newsNo;
        axios
          .get(`${API_URL}` + 'article/savelist' + `?id=${id}`)
          .then((res) => {
            let data = res.data;
            data.forEach((element) => {
              if (element.newsNo === newsNo) {
                this.saved = true;
              }
            });
          });
      }
    },
    like() {
      if (this.logged !== null) {
        this.liked = !this.liked;
        const scrapNo = this.newsInfo.scrapNo;
        axios
          .get(
            `${API_URL}` + 'article/like' + `?id=${id}` + `&scrapNo=${scrapNo}`
          )
          .then((res) => {
            if (res.data.message == 'success' && this.liked == true) {
              console.log('like', res.data.message);
            }
          });
      } else {
        this.alert = !this.alert;
      }
    },
    dislike() {
      if (this.logged !== null) {
        this.disliked = !this.disliked;
        const scrapNo = this.newsInfo.scrapNo;
        axios
          .get(
            `${API_URL}` +
              'article/dislike' +
              `?id=${id}` +
              `&scrapNo=${scrapNo}`
          )
          .then((res) => {
            if (res.data.message == 'success' && this.disliked == true) {
              console.log('dislike', res.data.message);
            }
          });
      } else {
        this.alert = !this.alert;
      }
    },
    likeCheck() {
      if (this.logged !== null) {
        const scrapNo = this.newsInfo.scrapNo;
        axios
          .get(
            `${API_URL}` +
              'article/islike' +
              `?id=${id}` +
              `&scrapNo=${scrapNo}`
          )
          .then((res) => {
            if (res.data.message === 'success') {
              this.liked = true;
            } else {
              this.liked = false;
            }
          });
        axios
          .get(
            `${API_URL}` +
              'article/isdislike' +
              `?id=${id}` +
              `&scrapNo=${scrapNo}`
          )
          .then((res) => {
            if (res.data.message === 'success') {
              this.disliked = true;
            } else {
              this.disliked = false;
            }
          });
      }
    },
  },
  created: function() {
    this.saveCheck(); // saved 상태체크
    this.likeCheck(); // liked/disliked 상태 체크
  },
  data: function() {
    return {
      news: this.newsInfo,
      swipeDirection: 'Right',
      saved: false,
      liked: false,
      disliked: false,
      play: false,
      logged: isLogged,
      alert: false, //저장
    };
  },
};
</script>

<style>
@media (max-width: 560px) {
  .hidden {
    display: none;
  }
}
.news-body-text {
  line-height: 2;
}
</style>
