<template>
  <v-container style="padding-left: 10px; padding-top: 0px">
    <v-form v-if="isLogged">
      <v-text-field
        style="padding: 0px"
        v-model="newComment"
        placeholder="댓글 추가..."
        ref="comment"
        @focus="isFocus = true"
        @blur="!isFocus"
      >
        <v-avatar size="40px" slot="prepend">
          <v-img
            v-if="myInfo.thumbnail_path != undefined"
            :src="myInfo.thumbnail_path"
          ></v-img>
        </v-avatar>
      </v-text-field>
      <v-row class="justify-end" no-gutters v-show="isFocus">
        <v-btn @click="cancel">취소</v-btn>
        <v-btn @click="addComment">작성</v-btn>
      </v-row>
    </v-form>
    <v-divider></v-divider>
    <BoardCommentDetail
      v-for="(com, index) in commentList"
      :key="index"
      :comment="com"
      :num="index"
      @delComment="removeComment"
      @updateComment="modifyComment"
    ></BoardCommentDetail>
  </v-container>
</template>

<script>
import {
  boardCommentList,
  boardCommentInsert,
  boardCommentDelete,
} from '@/api/board.js';
import { getInfo } from '@/api/user.js';
import BoardCommentDetail from '@/components/BoardCommentDetail.vue';
import { mapActions } from 'vuex';

export default {
  components: {
    BoardCommentDetail,
  },
  props: {
    boardPostNo: String,
    member: Object,
  },
  data() {
    return {
      myInfo: {},
      commentList: [],
      newComment: '',
      isFocus: false,
      isLogged: false,
    };
  },
  methods: {
    ...mapActions(['getUserInfo']),
    removeComment(index) {
      let comment = this.commentList.splice(index, 1);
      boardCommentDelete(
        comment[0].CommentNo,
        (response) => {
          if (response.data.message === 'success') {
            alert('해당 댓글 삭제에 성공했습니다.');
          } else {
            alert('해당 댓글 삭제에 실패했습니다.');
          }
        },
        (error) => {
          console.error(error);
          alert('게시물의 댓글 삭제 중 에러가 발생했습니다.');
        }
      );
    },
    cancel() {
      this.newComment = '';
      this.isFocus = false;
      this.$refs.comment.blur();
    },
    addComment() {
      let comm = {
        boardPostNo: this.boardPostNo,
        id: localStorage['id'],
        content: this.newComment,
      };
      boardCommentInsert(
        comm,
        (response) => {
          if (response.status >= 200 && response.status < 300) {
            this.commentList = response.data.reverse();
          } else {
            alert('댓글 등록이 실패하였습니다.');
          }
        },
        (error) => {
          console.error(error);
          alert('해당 게시물의 댓글을 등록하는 중 에러가 발생했습니다.');
        }
      );
      this.cancel();
    },
    modifyComment(comm, index) {
      this.commentList.splice(index, 1, comm);
    },
  },
  created() {
    this.myInfo = this.$store.getters.userProfile;
    if (this.myInfo.thumbnail_path == undefined) {
      let id = localStorage['id'];
      getInfo(
        id,
        (response) => {
          if (response.data.message == 'success') {
            this.myInfo = response.data['userInfo'];
            if (this.myInfo.thumbnail_path == null) {
              this.myInfo.thumbnail_path =
                'https://newha.s3.us-east-2.amazonaws.com/default-avatar.png';
            }
          } else {
            alert('댓글을 가져오는데 실패하였습니다.');
          }
        },
        (error) => {
          console.error(error);
          alert('해당 게시물의 댓글을 가져오는 중 에러가 발생했습니다.');
        }
      );
    }
    if (localStorage['access-token'] && localStorage['access-token'] !== '') {
      this.isLogged = true;
    }
    boardCommentList(
      this.boardPostNo,
      (response) => {
        if (response.status >= 200 && response.status < 300) {
          this.commentList = response.data.reverse();
        } else {
          alert('댓글을 가져오는데 실패하였습니다.');
        }
      },
      (error) => {
        console.error(error);
        alert('해당 게시물의 댓글을 가져오는 중 에러가 발생했습니다.');
      }
    );
  },
};
</script>

<style></style>
