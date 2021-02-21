<template>
  <v-expansion-panels v-model="panel">
    <v-expansion-panel class="panels">
      <v-tooltip bottom>
        <template v-slot:activator="{ on, attrs }">
          <v-expansion-panel-header
            hide-actions
            class="panels"
            v-bind="attrs"
            v-on="on"
          >
            <v-row>
              <v-btn color="red darken-3" 
                icon
                dark
                height="50px"
                class="close"
                @click="remove"
              >
                <v-icon medium>mdi-close-circle-outline</v-icon>
              </v-btn>
              <v-col class="ellipsis my-auto">{{ title }}
                <v-icon >mdi-arrow-down-drop-circle-outline</v-icon>
              </v-col>
            </v-row>
          </v-expansion-panel-header>
        </template>
        <span>클릭하시면 이 기사에 대한 오피니언과 해시태그를 추가할 수 있습니다.</span>
      </v-tooltip>
      <v-expansion-panel-content>
        <Tiptap @saveData="saveOther"></Tiptap>
      </v-expansion-panel-content>
    </v-expansion-panel>
  </v-expansion-panels>
</template>

<script>
import Tiptap from '@/components/Tiptap.vue';

export default {
  name: 'NewsForm',
  components: {
    Tiptap,
  },
  props: {
    news: Object,
    num: Number,
    focus: Boolean,
  },
  data() {
    return {
      panel: [],
      summary: '',
      tags: [],
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
      isSaveOnce: false,
    };
  },
  methods: {
    remove() {
      this.$emit('remove', this.num);
      this.panel = [];
    },
    saveOther(summary, fullTag) {
      this.$emit('save', summary, fullTag, this.num);
      this.panel = [];
    },
  },
  created() {
    if (this.news.url.indexOf('naver') != -1) {
      this.title = '네이버뉴스 ' + (this.num + 1);
    } else {
      this.title = '다른 뉴스 ' + this.num;
    }
  },
};
</script>

<style>
.panels {
  border: 1px solid gray;
  background-color: transparent;
  padding: 0px;
}
.left-border {
  border-left: 2px solid gray;
}
.no-margin {
  margin: 0 0 0 0;
  padding: 0 0 0 0;
  float: right;
}
.ellipsis {
  vertical-align: middle;
}
</style>
