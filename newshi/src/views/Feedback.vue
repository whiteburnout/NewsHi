<template>
  <div class="container">
    <h3>뉴하팀에 피드백 메일 보내기</h3>
    <v-form @submit.prevent="submit">
      <v-text-field
        v-model="email"
        :rules="emailRules"
        label="답장 받을 이메일"
        required
      ></v-text-field>

      <v-text-field
        v-model="title"
        label="제목"
        required
        autocapitalize="off"
      ></v-text-field>

      <v-textarea
        v-model="content"
        type="text"
        label="본문"
        required
      ></v-textarea>
      <br />
      <v-btn
        :disabled="!valid"
        color="rgb(252, 191, 73)"
        @click="submit"
        dark
        rounded
        elevation="2"
        >피드백 보내기</v-btn
      >
    </v-form> 
    <br>
    <!-- v-show="this.mailSent == true" -->
    <v-expand-x-transition>
      <v-alert
        v-show="expand"
        prominent
        type="success"
        bottom
      >
        {{ this.responseText }}
      </v-alert>
    </v-expand-x-transition>
    <v-expand-x-transition>
      <v-alert
        v-show="expand2"
        prominent
        type="error"
        bottom
      >
        {{ this.responseText }}
      </v-alert>
    </v-expand-x-transition>
  </div>
</template>

<script>
import axios from 'axios';
const API_FROM_AWS_API_GATEWAY = 'https://uferz9h0eg.execute-api.ap-northeast-2.amazonaws.com/default/newshifeedback';

export default {
  name: 'Feedback',
  methods: {
    isValid: function () {
      if (this.title == '' || this.content == '') {
        this.valid = false
      } else {
        this.valid = true
      }
    },
    submit: function () {
      axios.post(API_FROM_AWS_API_GATEWAY, JSON.stringify({
        email: this.email,
        title: this.title,
        content: this.content,
      }), {})
      .then(() => {
        this.responseText = '메일 발송 성공!';
        this.mailSent = true;
        this.expand = !this.expand;
        this.expand2 = false;   //성공하면 실패 메시지 지워줘야하기 때문에
      })
      .catch((e) => {
        this.responseText = '메일 발송 실패';
        this.mailSent = false;
        this.expand = false;  //실패하면 성공 메시지 지워줘야하기 때문에
        this.expand2 = !this.expand2;
        console.log(e)
      })
    },
  },
  data: function () {
    return {
      email: '',
      title: '',
      content: '',
      responseText: '',
      mailSent: false,
      valid: false,
      expand: false,
      expand2: false,
      emailRules: [
        v => !!v || 'E-mail is required',
        v => /.+@.+/.test(v) || 'E-mail must be valid',
      ],
    }
  },
  watch: {
    title: function () {
      this.isValid();
    },
    content: function () {
      this.isValid();
    },
  }
}
</script>