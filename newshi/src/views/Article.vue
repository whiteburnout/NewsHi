<template>
  <div>
    <ArticleDetail 
      :news-info="newsInfo"
    />
  </div>
</template>

<script>
import ArticleDetail from '../components/ArticleDetail'

export default {
  name: "Article",
  components: { 
    ArticleDetail,
  },
  data: function () {
    return {
      newsNo: 0,
      newsInfo : {},
    }
  },
  created: function() {
    this.newsNo = this.$route.params.newsNo;
    
    if(this.$route.params.newsInfo2 != undefined) {
      this.newsInfo = this.$route.params.newsInfo2;
      localStorage.setItem("newsInfo", JSON.stringify(this.newsInfo));
    } else {
      this.newsInfo = JSON.parse(localStorage["newsInfo"]);
    }
  }
};
</script>

<style>
.container {
  max-width: 800px;
}
.news-header {
  font-size: 12px;
  color: #999;
}
.news-title {
  /* margin-top: 30px; */
  font-size: 35px;
  font-weight: 900;
}
.news-info {
  margin: 20px auto;
}
.news-info > div {
  margin-right: .5rem;
  font-size: 15px;
}
.news-image-caption {
  color: #999;
  font-size: 12px;
  margin-bottom: 20px;
}
@media(max-width: 560px){
  .news-title {
    font-size: 28px;
    font-weight: 900;
  }
  .news-info {
    margin: 10px auto;
  }
}
</style>