<template>
  <div>
    <v-alert
      v-model="alert"
      dismissible
      color="#ff9800"
      border="bottom"
      elevation="4"
      colored-border
      icon="mdi-cursor-pointer"
      fixed
    >
      로그인이 필요한 서비스입니다.
    </v-alert>
  </div>
</template> 
<script>
  export default {
    data () {
      return {
        alert: true,
      }
    },
  }
</script>