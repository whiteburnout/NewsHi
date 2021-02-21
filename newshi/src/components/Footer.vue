<template>
  <v-card>
    <v-footer
      v-bind="localAttrs"
      :padless="padless"
    >
      <v-card
        flat
        tile
        width="100%"
        class="black text-center"
      >
        <v-card-text>
          <v-btn
            v-for="icon in icons"
            :key="icon"
            class="mx-4 white--text"
            icon
          >
            <v-icon size="24px">
              {{ icon }}
            </v-icon>
          </v-btn>
        </v-card-text>
        <v-divider></v-divider>
          <v-card-text class="white--text">
            로그인하고 전문가가 추천하는 최신 기사를 받아보세요.
          </v-card-text>
        </v-card>
      </v-footer>
    </v-card>
</template>

<script>
export default {
  data: () => ({
    icons: [
      'mdi-login-variant',
      'mdi-account-plus-outline',
      'mdi-close-outline',
    ],
    items: [
      'default',
      'absolute',
      'fixed',
    ],
    links: [
      '',
      '',
      '',
    ],
    padless: false,
    variant: 'fixed',
  }),
    computed: {
      localAttrs () {
        const attrs = {}
        if (this.variant === 'default') {
          attrs.absolute = false
          attrs.fixed = false
        } else {
          attrs[this.variant] = true
        }
        return attrs
      },
    },
  }
</script>

<style>
.v-footer{
  padding: 0 !important;
}
</style>