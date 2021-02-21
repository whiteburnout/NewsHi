<template>
  <v-hover v-slot="{ hover }">
    <v-card class="pl-3">
      <v-card-title class="card-title">
        <v-list style="padding: 0px;">
          <v-list-item style="padding: 0px;">
            <v-list-item-avatar>
              <v-img :src="member.thumbnail_path" :alt="member.name"></v-img>
            </v-list-item-avatar>
            <v-list-item-content>
              <v-list-item-title v-if="!isModify"
                ><a class="name" @click="goChannel">{{ member.name }}</a>
                <span class="time"> {{ time }} </span>
              </v-list-item-title>
              <v-list-item-subtitle class="content" v-if="!isModify">
                {{ comment.Content }}</v-list-item-subtitle
              >
              <v-list-item-subtitle class="content" v-else>
                <v-text-field
                  style="padding:opx; min-width: 240px"
                  v-model="alterComment"
                  placeholder="댓글 수정..."
                  ref="comment"
                  @focus="isFocus = true"
                  @blur="!isFocus"
                >
                </v-text-field>
              </v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
        </v-list>
        <v-menu offset-y>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              v-show="hover && isMyComment"
              style="position:absolute; right: 0%; top:5%;"
              icon
              v-bind="attrs"
              v-on="on"
            >
              <v-icon>mdi-dots-vertical</v-icon>
            </v-btn>
          </template>
          <v-list>
            <v-list-item v-if="!isModify">
              <v-list-item-title
                ><v-btn text @click="isModify = !isModify"
                  >수정</v-btn
                ></v-list-item-title
              >
            </v-list-item>
            <v-list-item v-else>
              <v-list-item-title
                ><v-btn text @click="cancel">취소</v-btn></v-list-item-title
              >
            </v-list-item>
            <v-list-item>
              <v-list-item-title
                ><v-btn text @click="deleteComment"
                  >삭제</v-btn
                ></v-list-item-title
              >
            </v-list-item>
          </v-list>
        </v-menu>
      </v-card-title>
      <v-card-text class="modify-btn"
        ><v-row
          style="position:absolute; right: 10%; bottom:15%;"
          v-show="isFocus"
        >
          <v-btn @click="cancel">취소</v-btn>
          <v-btn @click="modifyComment">수정</v-btn>
        </v-row></v-card-text
      >
    </v-card>
  </v-hover>
</template>

<script>
import { boardCommentUpdate } from '@/api/board.js';
import { userInfo } from '@/api/user.js';
export default {
  props: {
    comment: Object,
    num: Number,
  },
  data() {
    return {
      isMyComment: false,
      isModify: false,
      isFocus: false,
      alterComment: '',
      member: {},
      time: '',
    };
  },
  methods: {
    deleteComment() {
      this.$emit('delComment', this.num);
    },
    cancel() {
      this.alterComment = this.comment.content;
      this.isFocus = false;
      this.$refs.comment.blur();
      this.isModify = false;
    },
    modifyComment() {
      let comm = this.comment;
      comm.Content = this.alterComment;
      boardCommentUpdate(
        comm,
        (response) => {
          if (response.data.message === 'success') {
            this.$emit('updateComment', comm, this.num);
          } else {
            alert('댓글 수정이 실패하였습니다.');
          }
        },
        (error) => {
          console.error(error);
          alert('해당 게시물의 댓글을 수정하는 중 에러가 발생했습니다.');
        }
      );
      this.cancel();
    },
    goChannel() {
      this.$router.push(`/channel/${this.member.id}`);
    },
  },
  created() {
    //userNo를 통해 유저정보를 받아와야합니다.
    userInfo(
      this.comment.UserNo,
      (response) => {
        if (response.status >= 200 && response.status < 300) {
          this.member = {
            id: response.data['id'],
            name: response.data['name'],
            thumbnail_path: response.data['thumbnail_path'],
            platformtype: response.data['platformtype'],
          };
          if (this.member.thumbnail_path == null) {
            this.member.thumbnail_path =
              'https://newha.s3.us-east-2.amazonaws.com/default-avatar.png';
          }
          if (this.member.id === localStorage['id']) {
            this.isMyComment = true;
          } else {
            this.isMyComment = false;
          }
        } else {
          alert('큐레이터의 데이터를 받아오는데 실패했습니다.');
          this.$router.push(`/channel/${this.curator}`);
        }
      },
      (error) => {
        console.error(error);
        alert('큐레이터의 데이터를 받아오는 중 에러가 발생했습니다.');
      }
    );
    this.alterComment = this.comment.Content;
    this.time = this.$moment(this.comment.Date).fromNow();
  },
};
</script>

<style>
.card-title {
  max-width: 100px;
  padding: 0;
  padding-bottom: 0;
}
v-card {
  padding: 0;
}
.content {
  font-size: 15px;
}
.name {
  color: black;
  text-decoration: none;
  padding-right: 15px;
  font-size: 14px;
}
.time {
  font-size: 13px;
}
.modify-btn {
  padding: 8px;
}
</style>
