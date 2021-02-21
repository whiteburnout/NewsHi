<template>
  <v-app>
    <nav-bar></nav-bar>
    <br /><br />
    <router-view :key="$route.fullPath"></router-view>
    <AddNews></AddNews>
  </v-app>
</template>

<script>
import NavBar from './components/NavBar.vue';
import AddNews from './components/AddNews.vue';

export default {
  components: { 
    NavBar, 
    AddNews, 
  },
};
</script>
<style>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@500&family=Noto+Serif+KR:wght@700&display=swap');

html,
body {
  font-family: Spectral, 'Noto Serif KR', serif;
  font-weight: 900;
}

#app {
  font-family: Spectral, 'Noto Serif KR', serif;
}
</style>
