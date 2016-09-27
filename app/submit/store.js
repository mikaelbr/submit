const defaultState = {
    title: ''
};

const talk = (state = defaultState, action) => {
    switch (action.type) {

    case 'CHANGE_TITLE':
        return {
            title: action.text
        };

    default:
        return state;

    };
};

export default talk;
