<template>
  <div></div>
</template>

<script>
export default {
  computed: {
    getMember() {
      return this.$store.state.userProfile;
    },
  },
  watch: {
    getMember: function() {
      this.$emit("callScrap");
    },
  },
  created() {  
      },
      methods: {
        callScrap() {
          this.$emit("callScrap");
        }
      },
}
</script>

<style>

</style>