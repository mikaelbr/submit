import r from 'r-dom';
import reactMarkdown from 'react-markdown';
import { connect } from 'react-redux';
import { changeTitle } from './action';

function stateToMarkdown(title) {
    return `# ${title}`;
}

const submit = ({onTitleChange, title}) => (
    r.div({className: 'register'}, [
        r.h1('Submit'),
        r.input({type: 'text', placeholder: 'Your title', value: title, onChange: onTitleChange}),
        r(reactMarkdown, {className: 'markdown-preview', source: stateToMarkdown(title)})
    ])
);

const mapStateToProps = (state) => (
    {
        title: state.title
    }
);

const mapDispatchToProps = (dispatch) => (
    {
        onTitleChange: (ev) => {
            dispatch(changeTitle(ev.target.value));
        }
    }
);

export default connect(mapStateToProps, mapDispatchToProps)(submit);
