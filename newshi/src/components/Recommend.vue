<template>
 <v-sheet
    class="mx-auto"
    max-width="800"
  >
    <v-slide-group
      v-model="model"
      class="pa-4"
      center-active
    >
      <v-slide-item
        
        v-for="(user, idx) in users"
        :key="idx"
      >
        <v-card 
          class="d-flex flex-row ma-2"
          max-width="200"
          min-width="140"
          dismissable
        >
          <v-list-item-content 
            class="justify-center"
          >
            <div class="mx-auto text-center">
              <v-avatar
                color="brown"
              >
                <v-img v-if="user.thumnail_path !== undefined" :src="user.thumnail_path[0]"></v-img>
              </v-avatar>
              <h3 v-if="user.name !== undefined">{{ user.name[0] }}</h3>
              <span class="caption mt-1" 
                v-for="tag in user.tag"
                :key="tag"
              >
                #{{ tag }}
              </span>
              <br>
              <v-btn
                tile
                text
              >
                구독
              </v-btn>
            </div>
          </v-list-item-content>
        </v-card>
      </v-slide-item>
    </v-slide-group>
  </v-sheet>
</template>

<script>

export default {
  props: [
    'recommends',
  ],
  data: function () {
    return {
      model: null,
      users: this.recommends,
    }
  },
}
</script>

<style>

</style>