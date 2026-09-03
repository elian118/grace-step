import { dirname } from "path";
import { fileURLToPath } from "url";
import { FlatCompat } from "@eslint/eslintrc";

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

const compat = new FlatCompat({
  baseDirectory: __dirname,
});

const eslintConfig = [
  ...compat.config({
    extends: ["next/core-web-vitals", "next/typescript"],
    rules: {
      'max-len': [
        'error',
        {
          code: 120,
          tabWidth: 2,
          ignoreComments: true,
          ignoreTrailingComments: true,
          ignoreStrings: true,
          ignoreUrls: true,
          ignoreTemplateLiterals: true,
          ignorePatterns: '^import\\s.+\\sfrom\\s.+;$',
        }
      ],
      'no-use-before-define': 'off',
      'linebreak-style': 'off',
      camelcase: 'warn',
      'max-nested-callbacks': 'off',
      'class-methods-used': 'off',
      'import/prefer-default-export': 'off',
      'import/extensions': [
        'error',
        'ignorePackages',
        {
          js: 'never',
          ts: 'never',
          jsx: 'never',
          tsx: 'never',
        }
      ],
      'no-underscore-dangle': 0,
      // react
      'react/jsx-filename-extension': [2, {extensions: ['.js', '.jsx', '.ts', '.tsx']}],
      'react/require-default-props': 0,
      'react/jsx-props-no-spreading': 1,
      'react/jsx-uses-react': 'off',
      'react/react-in-jsx-scope': 'off',
      'react-hooks/rule-of-hooks': 'error',
      'react-hooks/exhaustive-deps': 'warn',
      // typescript
      '@typescript-eslint/no-use-before-define': 2,
      '@typescript-eslint/no-unused-vars': ['warn', { argsIgnorePattern: '^_' }],
    }
  }),
];

export default eslintConfig;
