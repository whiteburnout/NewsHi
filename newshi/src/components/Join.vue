<template>
  <v-card v-if="!isKakao">
    <v-toolbar color="#fcbf49">
      <v-btn icon @click="closeDialog">
        <v-icon>mdi-close</v-icon>
      </v-btn>
      <v-spacer></v-spacer>
      <v-toolbar-title> 회원가입</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-toolbar-items>
        <v-btn text @click="changeForm">
          로그인
        </v-btn>
      </v-toolbar-items>
    </v-toolbar>
    <v-form class="pa-6" @submit.prevent="onJoin">
      <ValidationProvider
        name="아이디"
        rules="required|email"
        v-slot="{ errors }"
      >
        <v-text-field
          v-model="id"
          :error-messages="errors"
          label="아이디"
          required
          autocapitalize="off"
        ></v-text-field>
        <v-btn v-show="!isDupEmailCheck" @click="emailCheck()"
          >아이디 인증</v-btn
        >
      </ValidationProvider>

      <v-text-field
        v-model="validNum"
        required
        v-show="isDupEmailCheck && !isEmailValid"
        placeholder="인증번호를 입력해주세요"
      ></v-text-field>
      <v-btn
        v-show="isDupEmailCheck && !isEmailValid"
        @click="emailValidCheck()"
        >인증번호 확인</v-btn
      >
      <v-btn v-show="isDupEmailCheck && !isEmailValid" @click="resendEmail()"
        >재발송</v-btn
      >

      <ValidationProvider
        name="이름"
        rules="required|min:2"
        v-slot="{ errors }"
      >
        <v-text-field
          v-model="name"
          :error-messages="errors"
          label="이름"
          required
          autocapitalize="off"
        ></v-text-field>
        <v-btn v-show="!isDupNameCheck" @click="nameCheck()">중복검사</v-btn>
        <v-btn disabled v-show="isDupNameCheck">검사완료</v-btn>
      </ValidationProvider>

      <ValidationProvider
        name="비밀번호"
        rules="required|password"
        v-slot="{ errors }"
      >
        <v-text-field
          v-model="password"
          type="password"
          :error-messages="errors"
          label="비밀번호"
          required
        ></v-text-field>
      </ValidationProvider>

      <ValidationProvider
        name="비밀번호 확인"
        rules="required|password|passwordConfirm:@joinPassword"
        v-slot="{ errors }"
      >
        <v-text-field
          v-model="passwordConfirm"
          type="password"
          :error-messages="errors"
          label="비밀번호 확인"
          required
        ></v-text-field>
      </ValidationProvider>
      <span><h4>관심 목록</h4></span>
      <v-row style="max-width=600px">
        <v-col>
          <v-sheet outlined>
            <v-chip-group
              v-model="tags"
              multiple
              active-class="yellow --text"
              column
            >
              <v-chip border large v-for="tag in tagName" :key="tag">
                {{ tag }}
              </v-chip>
            </v-chip-group>
          </v-sheet>
        </v-col>
      </v-row>
      <br />
      <v-btn color="#fcbf49" @click="onJoin">회원가입</v-btn>
    </v-form>
  </v-card>

  <v-card v-else>
    <v-toolbar color="#fcbf49">
      <v-btn icon @click="closeDialog">
        <v-icon>mdi-close</v-icon>
      </v-btn>
      <v-toolbar-title> 회원가입</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-toolbar-items>
        <v-btn text @click="changeForm">
          로그인
        </v-btn>
      </v-toolbar-items>
    </v-toolbar>
    <v-form class="pa-6" @submit.prevent="onKakaoJoin">
      <ValidationProvider
        name="KakaojoinId"
        rules="required|email"
        v-slot="{ errors }"
      >
        <v-text-field
          v-model="kakaoId"
          :error-messages="errors"
          label="아이디"
          required
          autocapitalize="off"
        ></v-text-field>
        <v-btn v-show="!isDupEmailCheck" @click="emailCheck()"
          >아이디 인증</v-btn
        >
      </ValidationProvider>

      <v-text-field
        v-model="validNum"
        required
        v-show="isDupEmailCheck"
        placeholder="인증번호를 입력해주세요"
      ></v-text-field>
      <v-btn
        v-show="isDupEmailCheck && !isEmailValid"
        @click="emailValidCheck()"
        >인증번호 확인</v-btn
      >
      <v-btn v-show="isDupEmailCheck && !isEmailValid" @click="resendEmail()"
        >재발송</v-btn
      >

      <span><h4>관심 목록</h4></span>
      <v-row style="max-width=600px">
        <v-col>
          <v-sheet outlined>
            <v-chip-group
              v-model="tags"
              multiple
              active-class="yellow --text"
              column
            >
              <v-chip border large v-for="tag in tagName" :key="tag">
                {{ tag }}
              </v-chip>
            </v-chip-group>
          </v-sheet>
        </v-col>
      </v-row>
      <br />
      <v-btn color="#fcbf49" @click="onKakaoJoin">회원가입</v-btn>
    </v-form>
  </v-card>
</template>

<script>
import {
  login,
  join,
  emailTest,
  emailValidTest,
  nameTest,
  socialLogin,
} from '@/api/user.js';
import { ValidationProvider } from 'vee-validate';
import { extend } from 'vee-validate';
import * as rules from 'vee-validate/dist/rules';

Object.keys(rules).forEach((rule) => {
  extend(rule, rules[rule]);
});
extend('password', {
  message: '숫자, 영어 소문자, 특수문자로 비밀번호를 구성해주세요.',
  validate: (value) => {
    return /^.*(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[$@$!%*#?&]).*$/.test(value);
  },
});
extend('passwordConfirm', {
  params: ['target'],
  validate(value, { target }) {
    return value === target;
  },
  message: '비밀번호가 일치하지 않습니다.',
});

export default {
  components: {
    ValidationProvider,
  },
  props: {
    isKakao: Boolean,
    user: Object,
  },
  data() {
    return {
      id: '',
      name: '',
      password: '',
      passwordConfirm: '',
      tags: [],
      isValid: false,
      isDupEmailCheck: false,
      isEmailValid: false,
      isDupNameCheck: false,
      validNum: '',
      validNumCheck: '',
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
      kakaoId: '',
      emailValidCnt: 0,
    };
  },
  watch: {
    id: function() {
      this.checkForm();
      this.isDupEmailCheck = false;
      this.isEmailValid = false;
    },
    name: function() {
      this.checkForm();
      this.isDupNameCheck = false;
    },
    password: function() {
      this.checkForm();
    },
    passwordConfirm: function() {
      this.checkForm();
    },
  },
  methods: {
    checkForm() {
      this.isValid =
        this.id && this.name && this.password && this.passwordConfirm;
    },
    emailCheck() {
      //   if (!validator.validate('joinId')) {
      //     this.emailValid = false;
      //     alert('입력하신 아이디는 이메일 형식이 아닙니다.');
      //     return;
      //   }
      let id = '';
      if (this.isKakao) {
        id = this.kakaoId;
      } else {
        id = this.id;
      }
      emailTest(
        id,
        (response) => {
          if (response.data.message === 'success') {
            this.isDupEmailCheck = true;
            emailValidTest(
              id,
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
          } else {
            this.isDupEmailCheck = false;
            alert(
              '입력하신 아이디는 이미 등록된 아이디입니다. 다시 한번 확인해주시기 바랍니다.'
            );
          }
        },
        (error) => {
          console.error(error);
          alert('아이디를 다시 한번 확인해주세요.');
        }
      );
    },
    emailValidCheck() {
      this.emailValidCnt += 1;
      if (this.validNumCheck == this.validNum) {
        this.isEmailValid = true;
        alert('이메일 인증이 완료되었습니다.');
      } else {
        if (this.emailValidCnt > 5) {
          this.isDupEmailCheck = false;
          this.isEmailValid = false;
          alert(
            '이메일 인증에 실패했습니다. 새롭게 이메일 인증을 시도해주세요.'
          );
        } else {
          alert('이메일 인증에 실패했습니다.' + this.emailValidCnt + '회');
        }
      }
    },
    resendEmail() {
      let id = '';
      if (this.isKakao) {
        id = this.kakaoId;
      } else {
        id = this.id;
      }
      emailTest(
        id,
        (response) => {
          if (response.data.message === 'success') {
            this.isDupEmailCheck = true;
            alert(
              '입력하신 아이디로 재발송하였습니다. 인증번호를 입력해주세요.'
            );
          } else {
            this.isDupEmailCheck = false;
            alert('재발송에 실패했습니다. 다시 한번 확인해주시기 바랍니다.');
          }
        },
        (error) => {
          console.error(error);
          alert('재발송 중 에러가 발생했습니다. 죄송합니다.');
        }
      );
    },
    nameCheck() {
      //   if (!validator.validate('joinId')) {
      //     this.emailValid = false;
      //     alert('입력하신 이름은 잘못된 형식입니다.');
      //     return;
      //   }
      nameTest(
        this.name,
        (response) => {
          if (response.data.message === 'success') {
            this.isDupNameCheck = true;
            alert('사용가능한 이름입니다.');
          } else {
            this.isDupNameCheck = false;
            alert('입력하신 이름은 이미 등록된 이름입니다.');
          }
        },
        (error) => {
          console.error(error);
          alert('이름을 다시 한번 확인해주세요.');
        }
      );
    },
    onJoin() {
      if (!this.isValid) {
        alert('모든 정보를 입력해주시기 바랍니다.');
        return;
      } else if (!this.isDupEmailCheck) {
        alert('이메일 인증을 해야 합니다.');
        return;
      } else if (!this.isEmailValid) {
        alert('이메일 인증번호를 검사해야 합니다.');
        return;
      } else if (!this.isDupNameCheck) {
        alert('이름 중복 검사를 해야합니다.');
        return;
      }
      let fullTag = '';
      for (let i = 0; i < this.tags.length; i++) {
        fullTag = fullTag + '#' + this.tagName[this.tags[i]];
        // this.tags[i] = this.tagName[this.tags[i]];
      }
      let info = {
        id: this.id,
        name: this.name,
        password: this.password,
        thumbnail_path: null,
        tags: fullTag,
      };

      join(
        info,
        (response) => {
          if (response.data.message === 'success') {
            alert('회원가입에 성공했습니다.');
            this.onSubmit();
          } else {
            this.isDupEmailCheck = false;
            this.isEmailValid = false;
            this.isDupNameCheck = false;
            alert('회원가입에 실패했습니다. 다시 한번 확인해주세요.');
          }
        },
        (error) => {
          console.error(error);
          alert('회원가입 중 오류가 발생했습니다.');
        }
      );
    },

    onSubmit() {
      let { id, password } = this;
      let info = {
        id,
        password,
      };
      localStorage.setItem('access-token', '');
      login(
        info,
        (response) => {
          if (response.data.message === 'success') {
            let token = response.data['access-token'];
            localStorage.setItem('access-token', token);
            localStorage.setItem('id', info.id);
            this.login();
          } else {
            this.isLoginError = true;
          }
        },
        (error) => {
          console.error(error);
          alert('아이디 또는 비밀번호가 일치하지 않습니다.');
        }
      );
    },
    onKakaoJoin() {
      let info = {
        id: this.kakaoId,
        name: this.user.name,
        thumbnail_path: this.user.thumbnail_path,
        password: null,
        tags: this.tags,
      };
      socialLogin(
        info,
        (response) => {
          if (response.data.message === 'success') {
            let token = response.data['access-token'];
            localStorage.setItem('access-token', token);
            localStorage.setItem('id', info.id);
            this.$store.commit('SET_LOGGED', true);
            this.$store.commit('SET_USER', info);
            this.$router.push('/');
          } else {
            this.isLoginError = true;
          }
        },
        (error) => {
          console.error(error);
          alert('아이디 또는 비밀번호가 일치하지 않습니다.');
        }
      );
    },
    closeDialog() {
      this.$emit('closeDialog');
    },
    changeForm() {
      this.$emit('changeLogin');
    },
    login() {
      this.$emit('login');
    },
  },
};
</script>

<style></style>
