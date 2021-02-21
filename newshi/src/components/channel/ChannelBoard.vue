<template>
  <v-container>
    <v-row v-if="isMyPage" >
        <v-col class="d-flex justify-end"> 
            <v-btn @click="addBoard">게시글 등록</v-btn>
        </v-col>
    </v-row>
    <v-row no-gutters v-show="hasList" v-for="(board, index) in list" :key="index">
        <v-col>
            <BoardInfo
            :board="board"
            :num="index"
            :member="member"
            :isMain="true"
            @delBoard="removeBoard"
            ></BoardInfo>
        </v-col>
    </v-row>
    <v-row no-gutters v-show="!hasList">
        <h2>게시글이 없습니다.</h2>
    </v-row>
  </v-container>
</template>

<script>
import BoardInfo from '@/components/BoardInfo.vue';
import { sidebarUser } from '@/api/user.js';
import { boardList, boardDelete } from '@/api/board.js';

export default {
  components: {
    BoardInfo,
  },
  data() {
    return {
      member: {},
      isMyPage: false,
      list: [],
      hasList: false,
      curator: '',
    };
  },
  methods: {
    addBoard() {
      this.$router.push('/addBoard');
    },
    removeBoard(index) {
      let board = this.list.splice(index, 1);
      boardDelete(
        board[0].boardPostNo,
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
    this.curator = this.$route.params.id;
    //유저 정보 받아오는 axios
    sidebarUser(
      this.curator,
      (response) => {
        if (response.status >= 200 && response.status < 300) {
          this.member = {
            id: this.curator,
            name: response.data['name'],
            userNo: response.data['userNo'],
            thumbnail_path: response.data['thumbnail_path'],
          };

          if (this.member.id === localStorage['id']) {
            this.isMyPage = true;
          } else {
            this.isMyPage = false;
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

    boardList(
      this.curator,
      (response) => {
        if (response.status >= 200 && response.status < 300) {
          if (response.data[0].message === '게시글이 없습니다.') {
            this.hasList = false;
          } else {
            this.hasList = true;
            this.list = response.data;
          }
        }
      },
      (error) => {
        console.error(error);
        alert('게시판 목록을 받아오는 중 에러가 발생했습니다.');
      }
    );
  },
}
</script>

<style>

</style>