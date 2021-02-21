<template>
  <v-container class="produce-bottom">
    <h2>뉴하 팀을 소개합니다🖐️</h2>
    <v-row>
      <v-col
        v-for="(item, i) in items"
        :key="i"
        cols="12"
        xs="12"
        sm="12"
        md="6"
      >
        <v-card :color="item.color" dark>
          <div class="d-flex flex-wrap justify-space-between">
            <div>
              <v-card-title class="headline" v-text="item.title"></v-card-title>
              <v-card-subtitle
                class="text"
                v-text="item.text"
              ></v-card-subtitle>

              <v-card-subtitle v-text="item.role"></v-card-subtitle>
              <v-card-actions>
                <v-btn icon v-if="item.link">
                  <a :href="item.link"><v-icon>mdi-github</v-icon></a>
                </v-btn>
                <v-btn icon v-if="item.blog">
                  <a :href="item.blog"><v-icon>mdi-blogger</v-icon></a>
                </v-btn>
              </v-card-actions>
            </div>

            <v-avatar class="ma-3" size="125" rounded>
              <v-img :src="item.src"></v-img>
            </v-avatar>
          </div>
        </v-card>
      </v-col>
    </v-row>
    <div class="container">
      <v-row>
        <br />
        <h2>NewsHi</h2>
        <p>
          뉴하는 '뉴스'와 안녕을 뜻하는 영어 'Hi'의 합성어입니다. 하루에만 새로
          생성되는 기사의 수는 20,000건이 넘는다고 합니다. 정보의 홍수 속에서,
          올바른 정보를 가려내는 것은 매우 힘들지만, 동시에 매우 중요한 일이기도
          합니다. 하지만, 현대인은 매우 바쁩니다. 이런 기사의 '더미' 속에서
          중요한 것들을 찾아내는 것은 소중한 시간을 낭비하게 합니다. '누구
          믿을만한 사람이 꼭 봐야하는 기사 좀 골라줬으면 좋겠다. 그것만이라도 꼭
          읽게.' 이런 니즈로부터 뉴하가 탄생하게 되었습니다.
        </p>
        <p>
          최근 떠오르는 유튜브, 페이스북, 인스타그램과 같은 SNS의 주요 특징은
          '구독', '팔로우'입니다. 이제 더 이상 모두에게 똑같은 정보가 아닌,
          각자에게 필요한 정보들을 선별적으로 받아들이는 시대입니다. 뉴하는
          이러한 흐름에 발맞춘 개인 맞춤 뉴스 큐레이팅 서비스를 제공합니다.
          중요한 정보를 선별하고 그것을 공유하는 큐레이터들과 양질의 기사들을
          제공받으려는 니즈를 가진 독자들을 연결해주는 플랫폼을 제공합니다.
        </p>
        <p>
          뉴하는 앱이 아닙니다. 또, 완전히 새로운 플랫폼이 아닙니다. 오히려 기존
          플랫폼에 누구보다 자연스럽게 스며들고자 합니다. 새로운 기능을
          제공하지만, 새롭게 설치하고 가입하는 복잡한 절차들은 최대한
          배제하였습니다. 간단하게 기사 URL만 복사해서 붙여넣으면 누구나 쉽게
          스크랩하고 공유할 수 있습니다. 받아보는 입장에서는 가독성좋고 선별된
          기사들을 카톡, 텔레그램, 페이스북 같은 익숙한 플랫폼 안에서 아주
          손쉽게 열어볼 수 있습니다.
        </p>
        <p>
          뉴하 서비스에 관심과 사랑을 가져주셔서 정말 감사드립니다. 뉴하와 함께
          무럭무럭 성장하는 뉴하인이 되시길 진심으로 바라겠습니다.
          감사합니다.<br />
          - 뉴하 팀 일동.
        </p>
      </v-row>
    </div>
  </v-container>
</template>

<script>
export default {
  name: 'Whoweare',
  data: () => ({
    items: [
      {
        color: '#1F7087',
        src: require('@/assets/images/5.jpg'),
        title: '천창민',
        role: '팀장, Front-End',
        link: 'https://github.com/chunawoos',
        text: '무엇이든 물어보살!😊',
      },
      {
        color: '#1F7087',
        src: require('@/assets/images/1.jpg'),
        title: '김재성',
        role: 'Front-End',
        link: 'https://github.com/JS-2',
        text: '술 좋아하고, 롤 못해요',
      },
      {
        color: '#952175',
        src: require('@/assets/images/2.jpg'),
        title: '김재승',
        role: 'Back-End',
        link: 'https://github.com/kimjaeseung',
        text: `롤💎, 백준 실버IV😢`,
      },
      {
        color: '#952175',
        src: require('@/assets/images/3.jpg'),
        title: '백태훈',
        role: 'Back-End',
        link: 'https://github.com/whiteburnout',
        text: '아이엠 태훈🌴',
      },
      {
        color: '#1F7087',
        src: require('@/assets/images/4.jpg'),
        title: '조성국',
        role: 'Front-End',
        link: 'https://github.com/likelionSungGuk',
        blog: 'https://likelionSungGuk.github.io/',
        text: '버그 잡는 파이어뱃🔥',
      },
    ],
  }),
};
</script>
<style>
img {
  object-fit: cover;
}
.v-card__subtitle {
  padding-bottom: 0;
}
.produce-bottom {
  padding-bottom: 60px;
}
</style>
