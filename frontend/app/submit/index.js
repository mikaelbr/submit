import r from 'r-dom';
import reactMarkdown from 'react-markdown';
import { connect } from 'react-redux';
import { changeTitle, changeIngress, changeAbstract } from './action';

function stateToMarkdown(title, ingress, abstract) {
    return `## ${title}\n\n**${ingress}**\n\n${abstract}`;
}

const mapStateToProps = (state) => (
    {
        title: state.title,
        ingress: state.ingress,
        abstract: state.abstract
    }
);

const mapDispatchToProps = (dispatch) => (
    {
        onTitleChange: (ev) => {
            dispatch(changeTitle(ev.target.value));
        },

        onIngressChange: (ev) => {
            dispatch(changeIngress(ev.target.value));
        },

        onAbstractChange: (ev) => {
            dispatch(changeAbstract(ev.target.value));
        }
    }
);

const submit = ({onTitleChange, onIngressChange, onAbstractChange, title, ingress, abstract}) => (
    r.div({className: 'submit'}, [
        r.div({className: 'submit__edit'}, [
            r.h1('Submit your talk here'),
            r.div({className: 'submit__section'}, [
                r.h2('Your talk'),
                r.div({className: 'submit__field'}, [
                    r.h3('The title of your talk'),
                    r.p('Select an expressive and snappy title that captures the content of your talk without being too long. Remember that the title must be attractive and should make people curious.'),
                    r.input({type: 'text', placeholder: 'The title of your talk or workshop', value: title, onChange: onTitleChange})
                ]),
                r.div({className: 'submit__field'}, [
                    r.h3('Ingress'),
                    r.p('Give the participants a teaser for your talk. Will be used for the ingress of the talk. Keep it short, one paragraph maximum.'),
                    r.textarea({placeholder: 'Ingress', value: ingress, onChange: onIngressChange})
                ]),
                r.div({className: 'submit__field'}, [
                    r.h3('Abstract'),
                    r.p('Give a concise description of the content and goals of your talk. Try not to exceed 300 words, as shorter and more to-the-point descriptions are more likely to be read by the participants.'),
                    r.textarea({placeholder: 'Your full abstract', value: abstract, onChange: onAbstractChange})
                ])
            ]),
            r.div({className: 'submit__section'}, [
                r.h2('Metadata about your talk')
            ]),
            r.div({className: 'submit__section'}, [
                r.h2('Who are you?')
            ])
        ]),
        r.div({className: 'submit__preview'}, [
            r.h1('Preview your talk here'),
            r.div({className: 'markdown-preview'}, [
                r.h2({className: 'title'}, title),
                r.p({className: 'ingress'}, ingress),
                r(reactMarkdown, {className: 'abstract', source: abstract})
            ])
        ])
    ])
);

export default connect(mapStateToProps, mapDispatchToProps)(submit);
