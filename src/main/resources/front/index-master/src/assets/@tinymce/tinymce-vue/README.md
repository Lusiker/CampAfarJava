# Official TinyMCE Vue component

## About

This package is a thin wrapper around [TinyMCE](https://github.com/tinymce/tinymce) to make it easier to use in a Vue application. If you need detailed documentation on TinyMCE itself head to the [TinyMCE Documentation](https://www.tiny.cloud/docs/).

For some quick demos, check out the [storybook](https://tinymce.github.io/tinymce-vue/).

## Installation
```sh
$ npm install @tinymce/tinymce-vue
```

## Usage

### Loading the component

First you have to load the component and how you do this depends on how the app you are developing is set up. If you are using some kind of bundle loader like `webpack`, `rollup` or `browserify` you can add the import like this:

```js
// es modules
import Editor from '@tinymce/tinymce-vue';
// commonjs require
// NOTE: default needed after require
var Editor = require('@tinymce/tinymce-vue').default;
```
If you aren't using a module loader and just adding the javascript file imports to your html files you will have to copy the `tinymce-vue.min.js` file, found in the `lib/browser` folder of the npm package, to your app and add something like this:

```html
<script src="path/to/tinymce-vue.min.js"></script>
```

You can then add the editor to the `components` property of your app:

```js
// This might look different depending on how you have set up your app
// but the important part is the components property
var app = new Vue({
  el: '#app',
  data: { /* Your data */ },
  components: {
    'tinymce-editor': Editor // <- Important part
  },
  methods: { /* Your methods */}
})
```

### Using the component in your templates

Use the editor in your templates like this:

```html
<tinymce-editor api-key="API_KEY" :init="{plugins: 'wordcount'}"></tinymce-editor>
```

### Configuring the editor

The editor accepts the following props:
* `disabled`: Using this prop that takes a boolean value you can dynamically set the editor into a "disabled" readonly mode or into the normal editable mode.
* `id`: An id for the editor so you can later grab the instance by using the `tinymce.get('ID')` method on tinymce, defaults to an automatically generated uuid. 
* `init`: Object sent to the `tinymce.init` method used to initialize the editor.
* `initial-value`: Initial value that the editor will be initialized with.
* `inline`: Shorthand for setting that the editor should be inline, `<tinymce-editor inline></tinymce-editor>` is the same as setting `{inline: true}` in the init.
* `tag-name`: Only used if the editor is inline, decides what element to initialize the editor on, defaults to `div`.
* `plugins`: Shorthand for setting what plugins you want to use, `<tinymce-editor plugins="foo bar"></tinymce-editor>` is the same as setting `{plugins: 'foo bar'}` in the init.
* `toolbar`: Shorthand for setting what toolbar items you want to show, `<tinymce-editor toolbar="foo bar"></tinymce-editor>` is the same as setting `{toolbar: 'foo bar'}` in the init.
* `model-events`: Change on what events you want to trigger the v-model events, defaults to `'change keyup'`. 
* `api-key`: Api key for TinyMCE cloud, more info below.
* `cloud-channel`: Cloud channel for TinyMCE Cloud, more info below.

None of the configuration props are **required** for the component to work - other than if you are using TinyMCE Cloud you will have to specify the `api-key` to get rid of the `This domain is not registered...` warning message.

### `v-model`

You can also use the `v-model` directive (more info in the [VueJS documentation](https://vuejs.org/v2/guide/forms.html)) on the editor to create a two-way data binding:

```html
<tinymce-editor v-model="content"></tinymce-editor>
```

### Event binding

You bind editor events via a shorthand prop on the editor, for example:

```html
<tinymce-editor @onSelectionChange="handlerFunction"></tinymce-editor>
```

Where the handler function will be called with the event and a reference to the editor.

Here is a full list of the events available:
<details>
<summary>All available events</summary>

* `onActivate`
* `onAddUndo`
* `onBeforeAddUndo`
* `onBeforeExecCommand`
* `onBeforeGetContent`
* `onBeforeRenderUI`
* `onBeforeSetContent`
* `onBeforePaste`
* `onBlur`
* `onChange`
* `onClearUndos`
* `onClick`
* `onContextMenu`
* `onCopy`
* `onCut`
* `onDblclick`
* `onDeactivate`
* `onDirty`
* `onDrag`
* `onDragDrop`
* `onDragEnd`
* `onDragGesture`
* `onDragOver`
* `onDrop`
* `onExecCommand`
* `onFocus`
* `onFocusIn`
* `onFocusOut`
* `onGetContent`
* `onHide`
* `onInit`
* `onKeyDown`
* `onKeyPress`
* `onKeyUp`
* `onLoadContent`
* `onMouseDown`
* `onMouseEnter`
* `onMouseLeave`
* `onMouseMove`
* `onMouseOut`
* `onMouseOver`
* `onMouseUp`
* `onNodeChange`
* `onObjectResizeStart`
* `onObjectResized`
* `onObjectSelected`
* `onPaste`
* `onPostProcess`
* `onPostRender`
* `onPreProcess`
* `onProgressState`
* `onRedo`
* `onRemove`
* `onReset`
* `onSaveContent`
* `onSelectionChange`
* `onSetAttrib`
* `onSetContent`
* `onShow`
* `onSubmit`
* `onUndo`
* `onVisualAid`
</details>

## Loading TinyMCE
### Auto-loading from TinyMCE Cloud
The `Editor` component needs TinyMCE to be globally available to work, but to make it as easy as possible it will automatically load [TinyMCE Cloud](https://www.tiny.cloud/docs/cloud-deployment-guide/) if it can't find TinyMCE available when the component has mounted. To get rid of the `This domain is not registered...` warning, sign up for the cloud and enter the api key like this:

```html
<tinymce-editor api-key='YOUR_API_KEY' :init="{/* your settings */}"></tinymce-editor>
```

You can also define what cloud channel you want to use, for more info on the different versions see the [documentation](https://www.tiny.cloud/docs/cloud-deployment-guide/editor-plugin-version/#devtestingandstablereleases).

### Loading TinyMCE by yourself

To opt out of using TinyMCE cloud you have to make TinyMCE globally available yourself. This can be done either by hosting the `tinymce.min.js` file by youself and adding a script tag to you HTML or, if you are using a module loader, installing TinyMCE with npm. For info on how to get TinyMCE working with module loaders check out [this page in the documentation](https://www.tinymce.com/docs/advanced/usage-with-module-loaders/).

### Development

This project uses [yarn](https://yarnpkg.com/en/) for development.

### Issues

Have you found an issue with `tinymce-vue` or do you have a feature request? Open up an [issue](https://github.com/tinymce/tinymce-vue/issues) and let us know or submit a [pull request](https://github.com/tinymce/tinymce-vue/pulls). *Note: for issues related to TinyMCE please visit the [TinyMCE repository](https://github.com/tinymce/tinymce).*