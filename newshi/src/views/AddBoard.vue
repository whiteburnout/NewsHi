<template>
  <v-container>
    <Tiptap @saveData="addBoard" @cancel="cancel"></Tiptap>
  </v-container>
</template>

<script>
import Tiptap from '@/components/TiptapBoard.vue';
import { boardInsert } from '@/api/board.js';

export default {
  components: {
    Tiptap,
  },
  methods: {
    addBoard(content, title) {
      const data = {
        content: content,
        title: title,
      };
      const id = localStorage['id'];
      boardInsert(
        data,
        id,
        (response) => {
          if (response.data.message === 'success') {
            alert('게시판 작성에 성공했습니다.');
            this.$router.push(`channel/${id}/board/`);
          } else {
            alert('게시판 작성에 실패했습니다.');
          }
        },
        (error) => {
          console.error(error);
          alert('게시판 작성 중 에러가 발생했습니다..');
        }
      );
    },
    cancel() {
      const id = localStorage['id'];
      this.$router.push(`channel/${id}/board/`);
    },
  },
};
</script>

<style></style>
