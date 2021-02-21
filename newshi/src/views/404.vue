<template>
  <v-container>
    <h2 class="text-center">404 Error</h2>
    <h3 class="text-center">잘못된 페이지 경로입니다.</h3>
    <a href="/">
      <div class="text-center center-icon">
        <h1>👻</h1>
      </div>
    </a>
    <div class="text-center">
      <router-link to='/'>
        <v-btn text>
          메인으로 돌아가기
        </v-btn>
      </router-link>
    </div>
  
  <div>
    <v-item-group
      v-model="model"
      class="pa-2 d-flex justify-center"
      mandatory
    >
      <v-item
        v-for="([icon, bp, size], i) in sizes"
        :key="i"
        :value="size"
      >
        <template v-slot:default="{ active, toggle }">
          <v-hover v-slot="{ hover }">
            <div
              :class="[(active || hover) ? 'text--primary' : 'text--secondary']"
              class="ma-3 text-center"
              style="cursor: pointer"
              @click="toggle"
            >
              <v-icon
                :color="(active || hover) ? 'grey darken-4' : 'grey lighten-1'"
                class="pa-1 mb-1"
                v-text="icon"
              ></v-icon>

              <div
                class="caption"
                v-text="bp"
              ></div>
            </div>
          </v-hover>
        </template>
      </v-item>
    </v-item-group>

    <div class="text-h5 mb-6 text-center">
      <code v-text="typeClass"></code>
    </div>

    <v-card
      class="d-flex align-center justify-center pa-4 mx-auto"
      max-width="550"
      min-height="76"
      outlined
    >
      <div :class="`text-${model}`">
        Example Heading
      </div>
    </v-card>
  </div>

  </v-container>
</template>

<script>

export default {
  name: 'notFound',
  data() {
    return {
      model: 'caption',
      sizes: [
        ['mdi-devices', 'all', 'caption'],
        ['mdi-cellphone-iphone', 'sm', 'body-2'],
        ['mdi-laptop', 'md', 'body-1'],
        ['mdi-monitor', 'lg', 'h6'],
        ['mdi-television', 'xl', 'h4'],
      ],
    }
  },
  computed: {
    typeClass () {
      const type = ['text', this.model]
      const [, breakpoint] = this.current

      if (breakpoint !== 'all') {
        type.splice(1, 0, breakpoint)
      }

      return `.${type.join('-')}`
    },
    current () {
      return this.sizes.find(size => size[2] === this.model)
    },
  },
}
</script>

<style>
.center-icon {
  max-width: 100%;
  margin: 1rem;
}

</style>