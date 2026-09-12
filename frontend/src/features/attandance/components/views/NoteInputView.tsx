import React from 'react';

const NoteInputView = (params: any) => {
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = e.target.value;
    params.node.setDataValue(params.colDef.field, newValue);
  };

  return (
    <div>
      <input type="text" className="input input-sm" value={params.value || ''} onChange={(e) => handleChange(e)} />
    </div>
  );
};

export default NoteInputView;
