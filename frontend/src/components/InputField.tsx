import { useState } from "react";

interface Props {
    type: string;
    placeholder: string;
    icon: string;
    value: string;
    onChange: (value: string) => void;
    name: string;
}

const InputField = ({ type, placeholder, icon, value, onChange, name }: Props) => {
  // State to toggle password visibility
  const [isPasswordShown, setIsPasswordShown] = useState(false);
  
  return (
    <div className="input-wrapper">
      <input
        type={isPasswordShown ? 'text' : type}
        placeholder={placeholder}
        className="input-field"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        name={name}
        required
      />
      <i className="material-symbols-outlined">{icon}</i>
      {type === 'password' && (
        <i onClick={() => setIsPasswordShown(prevState => !prevState)} className="material-symbols-outlined eye-icon">
          {isPasswordShown ? 'visibility' : 'visibility_off'}
        </i>
      )}
    </div>
  )
}

export default InputField;