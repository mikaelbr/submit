import r from 'r-dom';
import reactMarkdown from 'react-markdown';
import { connect } from 'react-redux';
import { changeTitle, changeIngress } from './action';

function stateToMarkdown(title, ingress) {
    return `# ${title}\n\n${ingress}`;
}

const mapStateToProps = (state) => (
    {
        title: state.title,
        ingress: state.ingress
    }
);

const mapDispatchToProps = (dispatch) => (
    {
        onTitleChange: (ev) => {
            dispatch(changeTitle(ev.target.value));
        },

        onIngressChange: (ev) => {
            dispatch(changeIngress(ev.target.value));
        }
    }
);

const submit = ({onTitleChange, onIngressChange, title, ingress}) => (
    r.div({className: 'submit'}, [
        r.div({className: 'submit__edit'}, [
            r.h1('Submit'),
            r.input({type: 'text', placeholder: 'Your title', value: title, onChange: onTitleChange}),
            r.input({type: 'text', placeholder: 'Ingress', value: ingress, onChange: onIngressChange})
        ]),
        r.div({className: 'submit__preview'}, [
            r(reactMarkdown, {className: 'markdown-preview', source: stateToMarkdown(title, ingress)})
        ])
    ])
);

export default connect(mapStateToProps, mapDispatchToProps)(submit);
