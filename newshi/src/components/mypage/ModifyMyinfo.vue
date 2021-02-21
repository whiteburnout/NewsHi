<template>
  <div>
    <v-container>
      <v-row>
        <v-col class="d-flex justify-center">
          <v-card width="100%">
            <v-card-title>비밀번호 변경</v-card-title>
            <v-card-subtitle>
              안전한 비밀번호로 내정보를 보호하세요.
            </v-card-subtitle>
            <v-card-text>
              <ValidationProvider
                name="password"
                rules="required|password"
                v-slot="{ errors }"
              >
                <v-text-field
                  v-model="old_password"
                  label="현재 비밀번호"
                  placeholder="Placeholder"
                  outlined
                  type="password"
                  required
                  :error-messages="errors"
                ></v-text-field>
              </ValidationProvider>
              <ValidationProvider
                name="new_password"
                rules="required|password"
                v-slot="{ errors }"
              >
                <v-text-field
                  v-model="new_password"
                  label="새 비밀번호"
                  placeholder="Placeholder"
                  outlined
                  type="password"
                  :error-messages="errors"
                ></v-text-field>
              </ValidationProvider>
              <ValidationProvider
                name="Password Confirm"
                rules="required|password|passwordConfirm:@new_password"
                v-slot="{ errors }"
              >
                <v-text-field
                  v-model="new_password_confirm"
                  label="새 비밀번호 확인"
                  placeholder="Placeholder"
                  outlined
                  type="password"
                  :error-messages="errors"
                ></v-text-field>
              </ValidationProvider>
            </v-card-text>
            <v-card-actions>
              <v-btn width="100%" dark @click="passwordChangeSubmit()"
                >확인</v-btn
              >
            </v-card-actions>
            <v-card-actions>
              <v-btn width="100%" @click="backPage">취소</v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import { ValidationProvider } from 'vee-validate';
import { extend } from 'vee-validate';
import * as rules from 'vee-validate/dist/rules';
import axios from 'axios';
import { mapActions } from 'vuex';
import { API_BASE_URL } from '@/config';

var old_check = false;
var new_check = false;

Object.keys(rules).forEach((rule) => {
  extend(rule, rules[rule]);
});
extend('password', {
  message: '숫자, 영어 소문자, 특수문자로 비밀번호를 구성해주세요.',
  validate: (value) => {
    if (/^.*(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[$@$!%*#?&]).*$/.test(value)) {
      old_check = true;
      return true;
    } else {
      old_check = false;
    }
  },
});
extend('passwordConfirm', {
  params: ['target'],
  validate(value, { target }) {
    if (value === target) {
      new_check = true;
      return true;
    } else {
      new_check = false;
    }
  },
  message: '비밀번호가 일치하지 않습니다.',
});

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
  methods: {
    ...mapActions(['logout', 'getUserInfo']),
    backPage() {
      this.$router.push('/mypage');
    },
    passwordChangeSubmit() {
      if (!old_check || !new_check) {
        alert('비밀번호를 확인해주세요.');
        return;
      }
      var frm = new FormData();
      frm.append('id', this.member.id);
      frm.append('newpassword', this.new_password);
      frm.append('oldpassword', this.old_password);

      axios
        .put(API_BASE_URL + 'updatePassword', frm, {
          headers: { 'Content-Type': 'multipart/form-data' },
        })
        .then(() => {
          this.logout();
          this.$router.go('/');
        });
    },
  },
  components: {
    ValidationProvider,
  },
  data() {
    return {
      old_password: '',
      new_password: '',
      new_password_confirm: '',
      member: {},
    };
  },
  created() {
    if (this.$store.getters.userProfile.id != undefined) {
      this.member = this.$store.getters.userProfile;
    }
  },
};
</script>

<style></style>
