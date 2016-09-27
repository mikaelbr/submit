export const changeTitle = (text) => {
    return {
        type: 'CHANGE_TITLE',
        text
    };
};

export const changeIngress = (text) => {
    return {
        type: 'CHANGE_INGRESS',
        text
    }
};
