<template>
  <!-- 富文本 -->
  <div>
    <editor v-model="content" :init="init" :disabled="disabled" ></editor>
  </div>
</template>


<script>
import tinymce from 'tinymce/tinymce';
import Editor from '@tinymce/tinymce-vue';
import 'tinymce/themes/silver/theme';
import 'tinymce/icons/default/icons';
//引用插件
import 'tinymce/plugins/link';
import 'tinymce/plugins/code';
import 'tinymce/plugins/table';
import 'tinymce/plugins/lists';
import 'tinymce/plugins/contextmenu';
import 'tinymce/plugins/wordcount';
import 'tinymce/plugins/colorpicker';
import 'tinymce/plugins/textcolor';


export default {
  components: {
    Editor
  },
  props: {
    //传入一个value，使组件支持v-model绑定
    value: {
      type: String,
      default: ''
    },
    disabled: {
      type: Boolean,
      default: false
    },
    plugins: {
      type: [String, Array],
      default:
        // ' anchor autolink autosave code codesample colorpicker colorpicker contextmenu directionality emoticons fullscreen hr image imagetools insertdatetime link lists   noneditable pagebreak paste preview print save searchreplace spellchecker tabfocus table template textcolor textpattern visualblocks visualchars wordcount'
        'link code table lists wordcount '
    },
    toolbar: {
      type: [String, Array],
      default:
        // 'undo redo | searchreplace | bold  italic | underline | strikethrough | alignleft  aligncenter alignright | outdent indent  blockquote  removeformat subscript superscript code codesample hr bullist numlist link image charmap preview anchor pagebreak insertdatetime  table  forecolor backcolor'
        'undo redo |  formatselect | bold italic underline | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | lists image media table | removeformat hr'
    }
  },
  data () {
    return {
      //初始化配置
      init: {
        language_url: "public/static/tinymce/langs/zh_CN.js",
        language: "zh_CN",
        skin_url: "public/static/tinymce/skins/ui/oxide",
        content_css: 'public/static/tinymce/skins/content/document/content.css',
        height: 1500,
        min_height: 500,
        max_height: 2000,
        toolbar_mode: "wrap",
        plugins: this.plugins,
        toolbar: this.toolbar,
        content_style: "p {margin: 5px 0;}",
        fontsize_formats: "12px 14px 16px 18px 24px 36px 48px 56px 72px",
        font_formats:
          "微软雅黑=Microsoft YaHei,Helvetica Neue,PingFang SC,sans-serif;苹果苹方=PingFang SC,Microsoft YaHei,sans-serif;宋体=simsun,serif;仿宋体=FangSong,serif;黑体=SimHei,sans-serif;Arial=arial,helvetica,sans-serif;Arial Black=arial black,avant garde;Book Antiqua=book antiqua,palatino;",
        branding: false,
        contextmenu: "undo redo | cut copy paste pastetext | selectall table", // 富文本右键菜单
      },

      myValue: this.value
    }
  },
  mounted () {
    tinymce.init({})
  },
  methods: {
    //添加相关的事件，可用的事件参照文档=> https://github.com/tinymce/tinymce-vue => All available events
    //需要什么事件可以自己增加
    onClick (e) {
      this.$emit('onClick', e, tinymce)
    },
    //可以添加一些自己的自定义事件，如清空内容
    clear () {
      this.myValue = ''
    }
  },
  watch: {
    value (newValue) {
      this.content = newValue;
    },
    myValue (newValue) {
      this.$emit('input', newValue);
    }
  }
}
</script>
<style scoped>
</style>
