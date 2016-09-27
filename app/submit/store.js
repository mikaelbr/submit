import { assign } from 'lodash';

const defaultState = {
    title: '',
    ingress: ''
};

const talk = (state = defaultState, action) => {
    switch (action.type) {

    case 'CHANGE_TITLE':
        return assign({}, state, { title: action.text });

    case 'CHANGE_INGRESS':
        return assign({}, state, { ingress: action.text });

    default:
        return state;

    };
};

export default talk;
