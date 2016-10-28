import { assign } from 'lodash';

const defaultState = {
    title: '',
    ingress: '',
    abstract: ''
};

const talk = (state = defaultState, action) => {
    switch (action.type) {

    case 'CHANGE_TITLE':
        return assign({}, state, { title: action.text });

    case 'CHANGE_INGRESS':
        return assign({}, state, { ingress: action.text });

    case 'CHANGE_ABSTRACT':
        return assign({}, state, { abstract: action.text });

    default:
        return state;

    };
};

export default talk;
