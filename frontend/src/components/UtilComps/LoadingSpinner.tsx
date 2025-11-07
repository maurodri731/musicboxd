export const LoadingSpinner = ({ size = 50, color = '#3498db' }) => {
  return (
    <div
      style={{
        width: `${size}px`,
        height: `${size}px`,
        border: `${size / 10}px solid #e0e0e0`,
        borderTop: `${size / 10}px solid ${color}`,
        borderRadius: '50%',
        animation: 'spin 1s linear infinite',
      }}
    />
  );
};