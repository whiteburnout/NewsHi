<template>
  <v-container>
    <h2>비밀번호 찾기</h2>
    <h3>비밀번호를 찾기 위해 등록해주신 이메일을 입력해주세요</h3>
    <v-form class="pa-6" @submit.prevent="findPw">
      <ValidationProvider
        name="confirmId"
        rules="required|email"
        v-slot="{ errors }"
      >
        <v-text-field
          v-model="id"
          :error-messages="errors"
          label="E-mail"
          required
          autocapitalize="off"
          :disabled="isEmailCheck"
        ></v-text-field>
      </ValidationProvider>

      <v-btn color="orange lighten-4" @click="findPw">비밀번호 찾기</v-btn>
    </v-form>
    <v-expand-transition>
      <v-form v-show="isEmailCheck" @submit.prevent="validCheck">
        <v-text-field
          v-model="validNum"
          label="인증번호"
          required
        ></v-text-field>
        <v-btn color="orange lighten-4" @click="validCheck">이메일 인증</v-btn>
      </v-form>
    </v-expand-transition>
  </v-container>
</template>

<script>
import { emailTest, emailValidTest } from '@/api/user.js';
import { ValidationProvider } from 'vee-validate';
import { extend } from 'vee-validate';
import * as rules from 'vee-validate/dist/rules';

Object.keys(rules).forEach((rule) => {
  extend(rule, rules[rule]);
});

export default {
  components: {
    ValidationProvider,
  },
  data() {
    return {
      id: '',
      isEmailCheck: false,
      validNum: '',
      validNumCheck: '',
    };
  },

  methods: {
    findPw() {
      emailTest(
        this.id,
        (response) => {
          if (response.data.message === 'success') {
            this.isEmailCheck = false;
            alert(
              '입력하신 아이디는 없는 아이디입니다. 다시 한번 확인해주시기 바랍니다.'
            );
          } else {
            this.isEmailCheck = true;
            emailValidTest(
              this.id,
              (resp) => {
                if (resp.status >= 200 && resp.status < 300) {
                  this.validNumCheck = resp.data['confirm'];
                } else {
                  alert('이메일 인증에 실패했습니다.');
                }
              },
              (error) => {
                console.log(error);
                alert('이메일 인증 중 에러가 발생했습니다.');
              }
            );

            this.validNumCheck = response.data['validNum'];
            alert(
              '입력하신 아이디로 인증메일을 발송하였습니다. 인증번호를 입력해주세요.'
            );
          }
        },
        (error) => {
          console.error(error);
          alert('비밀번호 찾기 중 오류가 났습니다.');
        }
      );
    },
    validCheck() {
      if (this.validNum == this.validNumCheck) {
        alert('인증에 성공했습니다.');
        this.$router.push({ name: 'ChangePw', params: { id: this.id } });
      } else {
        alert('인증에 실패했습니다. 다시 한번 확인해주세요');
        this.validNum = '';
      }
    },
  },
};
</script>

<style></style>
