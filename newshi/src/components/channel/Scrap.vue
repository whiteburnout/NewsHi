<template>
  <v-container>
    <v-row justify="center">
        <v-col v-for="(scrap, index) in scraps" :key="index" cols="auto">
            <v-card width='130' height='130' class="ma-1">
            <v-img :src="scrap.scrap_thumbnail" aspect-ratio="1">
                <div fill-height style="width:100%; height:100%;" class="d-flex justify-center align-center" else>
                  <v-container >
                    <v-row>
                      <v-col class="d-flex justify-center align-center">
                         <v-btn class="subtitle-2" @click="showNews(scrap.postNo)">보러가기</v-btn>
                      </v-col>
                    </v-row>
                    <v-row v-if="isMyPage==true">
                      <v-col class="d-flex justify-center align-center">
                        <v-dialog :retain-focus="false" width="500" v-model="dialog">
                          <template v-slot:activator="{ on, attrs }">
                            <v-btn class="subtitle-2" v-bind="attrs" v-on="on" @click="modifyBtn(scrap.name, scrap.postNo)">
                             수정하기
                            </v-btn>
                          </template>
                          <v-card>
                            <input type="text" v-model="modify_title" style="font-size:30px; width:100%; text-align:center">
                            <v-card-actions>
                              <v-spacer></v-spacer>
                              <v-btn color="primary" @click="modify()">
                                수정
                              </v-btn>
                              <v-btn color="primary" @click="refreshPage">
                                취소
                              </v-btn>
                            </v-card-actions>
                          </v-card>
                        </v-dialog>
                      </v-col>
                    </v-row>
                  </v-container>
                </div>
            </v-img>
            <div class="ellipsis subtitle-2 mt-2">{{scrap.name}}</div>
            </v-card>
        </v-col>
    </v-row>
    <v-row v-if="scraps[0]==undefined" height="100%">
        <v-col class="d-flex justify-center">
          포스트가 없습니다.
        </v-col>
    </v-row>
    </v-container>
</template>

<script>
import axios from 'axios';
import { API_BASE_URL } from '@/config';

export default {
  computed: {
    getMember() {
      return this.$store.state.userProfile;
    },
  },
  watch: {
    getMember: function(val) {
      this.member = val;
      if(this.member.id == this.$route.params.id){
        this.isMyPage = true;
      }
    },
    member: function() {
      if(this.member.id == this.$route.params.id){
        this.isMyPage = true;
      }
    }
  },
    props: {
      propsIsMyPage: {
        type: Boolean
      }
    },
    data() {
        return {
          isMyPage: Boolean,
          scraps: [],
          dialog: false,
          flag: false,
          member: {},
          modify_title: '',
          modify_postNo: 0,
        }
    },
    methods: {
      showNews(scrap_no) {
        this.$router.push('./' + this.$route.params.id + '/scrap/' + scrap_no);
      },
      modifyBtn(name, postNo) {
        this.modify_title = name;
        this.modify_postNo = postNo;
        this.dialog=true;
      },
      modify() {
        axios.put(API_BASE_URL + 'article/post', { id: this.$route.params.id, name: this.modify_title, postNo: this.modify_postNo})
        .then(() => {
          window.location.reload();
        });
      },
      refreshPage() {
        this.dialog=false;
      },
      getPostList() {
        axios.get(API_BASE_URL + 'article/post', 
        { params: { id: this.$route.params.id } },
      ).then((response) => { 
        let scraps = response.data;
        for(let i = 0; i < scraps.length; i++) {
          setTimeout(() => {
            axios.get(API_BASE_URL + 'article/scraplist', 
            { params: { postNo: scraps[i].postNo } },
          ).then((response) => {
            if(response.data[0] != undefined) { 
              scraps[i].scrap_thumbnail = response.data[0].new_image[0];
              this.scraps.push(scraps[i]);
            }
          });
          }, i * 10);
        }
        });
      }
    },
    created() {
      if(this.$store.getters.userProfile.id != undefined) {
      this.member = this.$store.getters.userProfile;
    }
      this.getPostList();
  },
}
</script>

<style>
    .scrap_dark{
    background-color:black;
    opacity: 0.4;
    width: 100%;
    height: 100%;
 }
 .ellipsis{
        width:100%;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        text-align: center;
    } 
</style>