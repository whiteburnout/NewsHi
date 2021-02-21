<template>
  <v-container style="padding: 0">
    <v-row no-gutters>
      <v-col>
        <BoardInfo
          :member="member"
          :board="board"
          :num="num"
          :isMain="false"
          @delBoard="removeBoard"
        ></BoardInfo>
      </v-col>
    </v-row>
    <v-divider></v-divider>
    <v-row style="padding:10px 0px 0px 12px" no-gutters
      ><h4>댓글 {{ board.boardCommentCount }}개</h4></v-row
    >
    <v-row no-gutters>
      <BoardComment
        :boardPostNo="board.boardPostNo"
        :member="member"
      ></BoardComment>
    </v-row>
  </v-container>
</template>

<script>
import BoardInfo from '@/components/BoardInfo.vue';
import BoardComment from '@/components/BoardComment.vue';
import { boardDelete } from '@/api/board.js';

export default {
  components: {
    BoardInfo,
    BoardComment,
  },
  // props: {
  //   member: Object,
  //   board: Object,
  //   num: Number,
  // },
  data() {
    return {
      member: {},
      board: {},
      num: 0,
    };
  },
  methods: {
    removeBoard() {
      boardDelete(
        this.board.boardPostNo,
        (response) => {
          if (response.data.message === 'success') {
            alert('게시물 삭제에 성공했습니다.');
          } else {
            alert('게시물 삭제에 실패했습니다.');
          }
        },
        (error) => {
          console.error(error);
          alert('게시물 삭제 중 에러가 발생했습니다.');
        }
      );
    },
  },
  created() {
    this.member = JSON.parse(this.$route.query.member);
    this.board = JSON.parse(this.$route.query.board);
    this.num = this.$route.query.num;
  },
};
</script>

<style></style>
