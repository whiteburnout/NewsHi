<template>
  <v-card>
    <v-card-text>
      <Tiptap :board="board" @saveData="updateBoard" @cancel="cancel"></Tiptap>
    </v-card-text>
  </v-card>
</template>

<script>
import Tiptap from '@/components/TiptapBoard.vue';
import { boardUpdate } from '@/api/board.js';

export default {
  components: {
    Tiptap,
  },
  data() {
    return {
      time: '',
      board: {},
      member: {},
    };
  },
  methods: {
    updateBoard(content, title) {
      let modifiedBoard = {
        boardPostNo: this.board.boardPostNo,
        title: title,
        date: this.board.date,
        visit_cnt: this.board.visit_cnt,
        userNo: this.board.userNo,
        content: content,
        is_notice: this.board.is_notice,
      };
      boardUpdate(
        modifiedBoard,
        (response) => {
          if (response.data.message === 'success') {
            alert('게시판 수정에 성공했습니다.');
            let id = localStorage['id'];
            this.$router.push(`/channel/${id}/board`);
          } else {
            alert('게시판 수정에 실패했습니다.');
          }
        },
        (error) => {
          console.error(error);
          alert('게시판 수정 중 에러가 발생했습니다..');
        }
      );
    },
    cancel() {
      const id = localStorage['id'];
      this.$router.push(`/channel/${id}/board`);
    },
  },
  created() {
    this.board = JSON.parse(this.$route.query.board);
    this.member = JSON.parse(this.$route.query.member);
    this.time = this.$moment(this.board.date).fromNow();
  },
};
</script>

<style></style>
