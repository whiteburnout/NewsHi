<template>
  <v-dialog v-model="addDialog" fullscreen v-if="isLogged">
    <v-overlay :value="overlay">
      <v-progress-circular :size="60" indeterminate></v-progress-circular>
    </v-overlay>
    <template v-slot:activator="{ on, attrs }">
      <v-btn
        v-if="isLogged"
        color="#fcbf49"
        dark
        fixed
        bottom
        right
        fab
        v-bind="attrs"
        v-on="on"
      >
        <v-icon>mdi-pencil</v-icon>
      </v-btn>
    </template>

    <v-card>
      <v-container>
        <v-toolbar>
          <v-btn icon @click="addDialog = !addDialog">
            <v-icon>mdi-close</v-icon>
          </v-btn>
          <v-spacer />
          <v-toolbar-title>포스트 만들기</v-toolbar-title>
          <v-spacer />
        </v-toolbar>

        <v-form class="pa-6" @submit.prevent="registURL">
          <ValidationProvider name="url" rules="url" v-slot="{ errors }">
            <v-text-field
              v-model="url"
              :error-messages="errors"
              required
              placeholder="URL Ctrl+C 복사 Ctrl+V 붙여넣기"
              autocapitalize="off"
            ></v-text-field>
          </ValidationProvider>
          <v-btn color="#fcbf49" dark rounded @click="registURL">
            저장
          </v-btn>
        </v-form>
        <NewsForm
          v-for="(news, index) in post"
          :key="index"
          :num="index"
          :news="news"
          :focus="focus[index]"
          @remove="removeURL"
          @save="saveOther"
        ></NewsForm>
        <v-form class="pa-6" @submit.prevent="registPost">
          <v-text-field
            v-model="postName"
            required
            placeholder="포스트 제목을 작성해주세요"
          ></v-text-field>
          <v-btn color="#fcbf49" dark rounded @click="registPost">등록 </v-btn>
        </v-form>
      </v-container>
    </v-card>
  </v-dialog>
</template>

<script>
import { ValidationProvider } from 'vee-validate';
import { extend } from 'vee-validate';
import * as rules from 'vee-validate/dist/rules';
import NewsForm from '@/components/NewsForm.vue';
import { saveArticle } from '@/api/article.js';
Object.keys(rules).forEach((rule) => {
  extend(rule, rules[rule]);
});
extend('url', {
  message: 'URL 형식이 아닙니다. 다시 한번 확인해주세요.',
  validate: (value) => {
    return /(http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!]))?/i.test(
      value
    );
  },
});
export default {
  components: {
    ValidationProvider,
    NewsForm,
  },
  data() {
    return {
      addDialog: null,
      url: '',
      post: [],
      focus: [],
      postName: '',
      overlay: false,
      isLogged: false,
    };
  },
  methods: {
    registURL() {
      if (this.url === '') return;
      let news = {
        url: this.url,
        summary: '',
        tags: [],
      };
      this.post.push(news);
      this.focus.push(false);
      this.url = '';
    },
    registPost() {
      if (this.postName === '' || this.postName == null) {
        alert('포스트 제목을 작성해주세요');
        return;
      }
      for (let i = 0; i < this.post.length; i++) {
        if (this.post[i].summary == '' || this.post[i].tags == []) {
          let num = i + 1;
          alert(
            num + '번째 기사가 아직 저장되지 않았습니다. 확인해주시기 바랍니다.'
          );
          this.focus[i] = true;
          return;
        }
        let news = {
          url: this.post[i].url,
          summary: this.post[i].summary,
          tags: this.post[i].tags,
          id: localStorage['id'],
          name: this.postName,
        };
        this.post.splice(i, 1, news);
      }
      for (let i = 0; i < this.post.length; i++) {
        if (this.post[i].summary == '<p></p>') {
          this.post[i].summary = '';
        }
      }
      this.overlay = true;
      saveArticle(
        this.post,
        (response) => {
          this.overlay = false;
          if (response.data.message === 'success') {
            alert('포스트 만들기 성공!');
            let no = response.data.postNo;
            this.addDialog = false;
            this.$router.push({
              name: 'Link',
              params: {
                postNo: no,
                postName: this.postName,
                tags: this.post[0].tags,
              },
            });
          } else {
            alert('포스트 만들기 실패');
          }
        },
        (error) => {
          console.error(error);
          alert('포스트 생성 중 에러가 발생했습니다.');
          this.overlay = false;
        }
      );
    },
    removeURL(index) {
      this.post.splice(index, 1);
      this.focus.splice(index, 1);
    },
    saveOther(summary, tags, index) {
      let news = {
        url: this.post[index].url,
        summary: summary,
        tags: tags,
      };
      this.post.splice(index, 1, news);
      this.focus[index] = false;
    },
  },
  created() {
    if (
      localStorage['access-token'] === undefined ||
      localStorage['access-token'] === null ||
      localStorage['access-token'] === ''
    ) {
      this.isLogged = false;
    } else this.isLogged = true;
  },
};
</script>

<style></style>
