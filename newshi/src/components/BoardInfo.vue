<template>
  <v-hover v-slot="{ hover }">
    <v-card>
      <v-card-title class="card-title">
        <v-list style="padding: 0px">
          <v-list-item style="padding-left:12px; padding-bottom:0px">
            <v-list-item-avatar>
              <v-img :src="member.thumbnail_path" :alt="member.name"></v-img>
            </v-list-item-avatar>
            <v-list-item-content>
              <v-list-item-title
                ><a class="name" @click="goChannel">{{ member.name }}</a>
                <span class="time"> {{ time }} </span>
              </v-list-item-title>
              <v-list-item-subtitle v-text="board.title"></v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
        </v-list>
        <v-menu offset-y>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              style="position:absolute; right: 0%; top:5%;"
              icon
              v-show="hover && isMyPage"
              v-bind="attrs"
              v-on="on"
            >
              <v-icon>mdi-dots-vertical</v-icon>
            </v-btn>
          </template>
          <v-list>
            <v-list-item>
              <v-list-item-title
                ><v-btn text @click="modifyBoard"
                  >수정</v-btn
                ></v-list-item-title
              >
              <v-list-item-title
                ><v-btn text @click="deleteBoard"
                  >삭제</v-btn
                ></v-list-item-title
              >
            </v-list-item>
          </v-list>
        </v-menu>
      </v-card-title>
      <v-card-text class="main-content card-text" v-html="content.front">
      </v-card-text>
      <a
        class="main-content"
        v-show="!isDetail && !isEmpty"
        @click="isDetail = !isDetail"
        >자세히 보기</a
      >
      <v-card-text
        class="main-content card-text"
        v-if="isDetail"
        v-html="content.behind"
      >
      </v-card-text>
      <a
        class="main-content"
        v-show="isDetail & !isEmpty"
        @click="isDetail = !isDetail"
        >간략히 보기</a
      >
      <v-card-actions v-if="isMain">
        <v-btn style="padding-left: 66px;" x-small icon @click="boardDetail"
          ><v-icon @click="boardDetail">mdi-comment-text</v-icon></v-btn
        >
        {{ board.commentCnt }}
      </v-card-actions>
    </v-card>
  </v-hover>
</template>

<script>
export default {
  props: {
    member: Object,
    board: Object,
    num: Number,
    isMain: Boolean,
  },
  data() {
    return {
      isDetail: false,
      content: {
        front: '',
        behind: '',
      },
      isMyPage: false,
      isEmpty: false,
      time: '',
    };
  },
  methods: {
    goChannel() {
      this.$router.push({ name: 'Channel', params: { id: this.member.id } });
    },
    boardDetail() {
      this.$router.push({
        name: 'BoardDetail',
        query: {
          board: JSON.stringify(this.board),
          member: JSON.stringify(this.member),
          num: this.num,
        },
      });
    },
    modifyBoard() {
      this.$router.push({
        name: 'ModifyBoard',
        query: {
          board: JSON.stringify(this.board),
          member: JSON.stringify(this.member),
        },
      });
    },
    deleteBoard() {
      this.$emit('delBoard', this.num);
    },
  },
  created() {
    let locP = this.board.content.indexOf('</p>'); //</p>
    let locH = this.board.content.indexOf('</H'); //</H
    if (
      (locP !== -1 && locH !== -1 && locP < locH) ||
      (locH === -1 && locP !== -1)
    ) {
      this.content.front = this.board.content.substring(0, locP + 4);
      this.content.behind = this.board.content.substring(
        locP + 4,
        this.board.content.length
      );
    } else {
      this.content.front = this.board.content.substring(0, locH + 5);
      this.content.behind = this.board.content.substring(
        locH + 5,
        this.board.content.length
      );
    }
    if (this.content.behind === '') {
      this.isEmpty = true;
    }
    if (this.member.id === localStorage['id']) {
      this.isMyPage = true;
    }
    this.time = this.$moment(this.board.date).fromNow();
  },
};
</script>

<style>
.card-title {
  padding-left: 0px;
}
.main-content {
  font-size: 16px;
  padding-left: 66px;
}
.card-text {
  padding-top: 0px;
  padding-bottom: 0px;
}
</style>
