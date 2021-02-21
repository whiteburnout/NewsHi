<template>
  <v-container>
    <v-row>
      <v-form>
        <v-text-field
          class="board-title"
          v-model="title"
          placeholder="제목을 작성해주세요"
        >
        </v-text-field>
      </v-form>
    </v-row>
    <div class="editor">
      <Modal ref="ytmodal" @onConfirm="addCommand" />
      <editor-menu-bar :editor="editor" v-slot="{ commands, isActive }">
        <div class="menubar">
          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.bold() }"
            @click="commands.bold"
          >
            <img class="icon" src="@/assets/tiptap/icons/bold.svg" alt="" />
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.italic() }"
            @click="commands.italic"
          >
            <img class="icon" src="@/assets/tiptap/icons/italic.svg" alt="" />
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.strike() }"
            @click="commands.strike"
          >
            <img class="icon" src="@/assets/tiptap/icons/strike.svg" alt="" />
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.underline() }"
            @click="commands.underline"
          >
            <img
              class="icon"
              src="@/assets/tiptap/icons/underline.svg"
              alt=""
            />
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.code() }"
            @click="commands.code"
          >
            <img class="icon" src="@/assets/tiptap/icons/code.svg" alt="" />
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.paragraph() }"
            @click="commands.paragraph"
          >
            <img
              class="icon"
              src="@/assets/tiptap/icons/paragraph.svg"
              alt=""
            />
          </button>

          <button
            class="menubar__button H"
            :class="{ 'is-active': isActive.heading({ level: 1 }) }"
            @click="commands.heading({ level: 1 })"
          >
            H1
          </button>

          <button
            class="menubar__button H"
            :class="{ 'is-active': isActive.heading({ level: 2 }) }"
            @click="commands.heading({ level: 2 })"
          >
            H2
          </button>

          <button
            class="menubar__button H"
            :class="{ 'is-active': isActive.heading({ level: 3 }) }"
            @click="commands.heading({ level: 3 })"
          >
            H3
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.bullet_list() }"
            @click="commands.bullet_list"
          >
            <img class="icon" src="@/assets/tiptap/icons/ul.svg" alt="" />
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.ordered_list() }"
            @click="commands.ordered_list"
          >
            <img class="icon" src="@/assets/tiptap/icons/ol.svg" alt="" />
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.blockquote() }"
            @click="commands.blockquote"
          >
            <img class="icon" src="@/assets/tiptap/icons/quote.svg" alt="" />
          </button>

          <button
            class="menubar__button"
            :class="{ 'is-active': isActive.code_block() }"
            @click="commands.code_block"
          >
            <img class="icon" src="@/assets/tiptap/icons/code.svg" alt="" />
          </button>

          <button class="menubar__button" @click="commands.horizontal_rule">
            <img class="icon" src="@/assets/tiptap/icons/hr.svg" alt="" />
          </button>

          <button class="menubar__button" @click="commands.undo">
            <img class="icon" src="@/assets/tiptap/icons/undo.svg" alt="" />
          </button>

          <button class="menubar__button" @click="commands.redo">
            <img class="icon" src="@/assets/tiptap/icons/redo.svg" alt="" />
          </button>

          <button class="menubar__button" @click="openModal(commands.image)">
            <img class="icon" src="@/assets/tiptap/icons/image.svg" alt="" />
          </button>
        </div>
      </editor-menu-bar>
      <editor-menu-bubble
        :editor="editor"
        @hide="hideLinkMenu"
        v-slot="{ commands, isActive, getMarkAttrs, menu }"
      >
        <div
          class="menububble"
          :class="{ 'is-active': menu.isActive }"
          :style="`left: ${menu.left}px; bottom: ${menu.bottom}px;`"
        >
          <form
            class="menububble__form"
            v-if="linkMenuIsActive"
            @submit.prevent="setLinkUrl(commands.link, linkUrl)"
          >
            <input
              class="menububble__input"
              type="text"
              v-model="linkUrl"
              placeholder="https://"
              ref="linkInput"
              @keydown.esc="hideLinkMenu"
            />
            <button
              class="menububble__button"
              @click="setLinkUrl(commands.link, null)"
              type="button"
            >
              <img class="icon" src="@/assets/tiptap/icons/remove.svg" alt="" />
            </button>
          </form>

          <template v-else>
            <button
              class="menububble__button"
              @click="showLinkMenu(getMarkAttrs('link'))"
              :class="{ 'is-active': isActive.link() }"
            >
              <span>{{ isActive.link() ? 'Update Link' : 'Add Link' }}</span>
              <img class="icon" src="@/assets/tiptap/icons/link.svg" alt="" />
            </button>
          </template>
        </div>
      </editor-menu-bubble>
      <v-divider dark></v-divider>
      <editor-content class="editor__content" :editor="editor" />
    </div>

    <v-row>
      <v-col class="d-flex flex-row-reverse ma-1">
        <v-btn class="ml-2" @click="cancel">취소</v-btn>
        <v-btn class="ml-2" @click="save">등록</v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { Editor, EditorContent, EditorMenuBar, EditorMenuBubble } from 'tiptap';
import {
  Blockquote,
  CodeBlock,
  HardBreak,
  Heading,
  HorizontalRule,
  OrderedList,
  BulletList,
  ListItem,
  TodoItem,
  TodoList,
  Bold,
  Code,
  Italic,
  Link,
  Strike,
  Underline,
  History,
  Image,
} from 'tiptap-extensions';
import Modal from './Modal';

export default {
  components: {
    EditorContent,
    EditorMenuBar,
    EditorMenuBubble,
    Modal,
  },
  props: {
    board: Object,
  },
  data() {
    return {
      editor: new Editor({
        extensions: [
          new Blockquote(),
          new BulletList(),
          new CodeBlock(),
          new HardBreak(),
          new Heading({ levels: [1, 2, 3] }),
          new HorizontalRule(),
          new ListItem(),
          new OrderedList(),
          new TodoItem(),
          new TodoList(),
          new Link(),
          new Bold(),
          new Code(),
          new Italic(),
          new Strike(),
          new Underline(),
          new History(),
          new Image(),
        ],
        content: ``,
      }),
      linkUrl: null,
      linkMenuIsActive: false,
      title: '',
    };
  },
  created() {
    if (this.board !== undefined) {
      this.editor.setContent(this.board.content);
      this.title = this.board.title;
    }
  },
  beforeDestroy() {
    this.editor.destroy();
  },
  methods: {
    cancel() {
      this.$emit('cancel');
    },
    save() {
      this.$emit('saveData', this.editor.getHTML(), this.title);
    },
    showLinkMenu(attrs) {
      this.linkUrl = attrs.href;
      this.linkMenuIsActive = true;
      this.$nextTick(() => {
        this.$refs.linkInput.focus();
      });
    },
    hideLinkMenu() {
      this.linkUrl = null;
      this.linkMenuIsActive = false;
    },
    setLinkUrl(command, url) {
      command({ href: url });
      this.hideLinkMenu();
    },
    showImagePrompt(command) {
      const src = prompt('Enter the url of your image here');
      if (src !== null) {
        command({ src });
      }
    },
    openModal(command) {
      this.$refs.ytmodal.showModal(command);
    },
    addCommand(data) {
      if (data.command !== null) {
        data.command(data.data);
      }
    },
  },
};
</script>
<style lang="scss" scope>
.menubar {
  border-bottom: 2px solid gray;
  background-color: gray;
  button {
    border: 1px black;
    border-style: solid;
    background-color: white;
  }
}
.H {
  width: 34.39px;
  height: 23.6px;
  font-size: 12px;
  text-align: top;
}
.icon {
  position: relative;
  display: inline-block;
  vertical-align: middle;
  width: 0.8rem;
  height: 0.8rem;
  margin: 0 0.3rem;
  top: -0.05rem;
  fill: currentColor;
  // &.has-align-fix {
  // 	top: -.1rem;
  // }

  &__svg {
    display: inline-block;
    vertical-align: top;
    width: 100%;
    height: 100%;
  }

  &:first-child {
    margin-left: 0;
  }

  &:last-child {
    margin-right: 0;
  }
}
.editor {
  border: 2px solid gray;
  margin: 0px;
  width: 100%;
}
.editor__content {
  min-height: 300px;
  padding: 10px;
}
// svg sprite
body > svg,
.icon use > svg,
symbol {
  path,
  rect,
  circle,
  g {
    fill: currentColor;
    stroke: none;
  }

  *[d='M0 0h24v24H0z'] {
    display: none;
  }
}
.board-title {
  padding-left: 5%;
  padding-bottom: 10px;
  min-width: 350px;
}
.menubar {
  padding-left: 13px;
}
</style>
