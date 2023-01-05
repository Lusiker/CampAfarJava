import { Assertions, GeneralSteps, Logger, Pipeline } from '@ephox/agar';
import { UnitTest } from '@ephox/bedrock';
import { setup } from '../alien/Setup';
UnitTest.asynctest('InitTest', function (success, failure) {
    Pipeline.async({}, [
        Logger.t('Should be able to setup editor', GeneralSteps.sequence([
            setup({}, function (editor, viewModel, done) {
                done();
            })
        ])),
        Logger.t('Should be able to setup editor in inline mode via inline prop', GeneralSteps.sequence([
            setup({ inline: true }, function (editor, viewModel, done) {
                Assertions.assertEq('Editor should be inline', true, editor.inline);
                done();
            })
        ])),
        Logger.t('Should be able to setup editor in inline mode via init data', GeneralSteps.sequence([
            setup({ init: { inline: true } }, function (editor, viewModel, done) {
                Assertions.assertEq('Editor should be inline', true, editor.inline);
                done();
            })
        ])),
    ], success, failure);
});
