<template>
  <v-container>
    <v-toolbar
      flat
      dense
    >
      <v-tabs v-model="tab" center-active >
        <v-tabs-slider color="transparent"></v-tabs-slider>
          <v-tab
            v-for="(newsItem, index) in newsItems"
            :key="index"
            :ripple="false"
          >
            <v-list-item-avatar circle
              style="width: 50px; height: 50px;"
            >
              <v-img :src="newsItem.avatar"></v-img>
            </v-list-item-avatar>
          </v-tab>
      </v-tabs>
    </v-toolbar>

    <v-tabs-items v-model="tab">
      <v-tab-item
        v-for="(newsItem, idx) in newsItems"
        :key="newsItem+idx"
      >
      <p class="mt-2">{{ newsItem.name }} 님의 오늘의 뉴하!</p>
      <hr>
        <v-list>
          <template v-for="(newsInfo, i) in newsItem.newsLists">
            <v-list-item
              class="main-list"
              :key="newsInfo+i"
              @click="move(newsInfo)"
            >
              <v-list-item-avatar rounded >
                <v-img :src="newsInfo.image_path" centered></v-img>
              </v-list-item-avatar>
              <v-list-item-content>
                  <v-list-item-title v-html="newsInfo.title"></v-list-item-title>
                  <v-list-item-subtitle v-html="newsInfo.company"></v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
          </template>
        </v-list>
      </v-tab-item>
    </v-tabs-items>
  </v-container>
</template>

<script>

export default {
  props: [
    'newsInfos'
  ],
  methods: {
    move(newsInfo){
      this.$router.push({name: 'Article', params: {newsNo: newsInfo.newsNo, newsInfo2: newsInfo} })
    },
  },
  data: function () {
    return {
        tab: null,
        newsItems: this.newsInfos,
      }
  },
  created: function () {
  }
}
</script>

<style>
.container {
  max-width: 800px;
}
.theme--dark.v-sheet{
  background-color: #121212;
}
.theme--dark.v-tabs{
  background-color: #121212;
}
.theme--dark.v-tabs-items {
  background-color: #121212 !important;
}
.v-slide-group__prev--disabled{
  display: none !important;
}
.v-tab {
  padding:0 0 !important;
}
.main-list {
  padding-left: 0 !important;
}

</style>